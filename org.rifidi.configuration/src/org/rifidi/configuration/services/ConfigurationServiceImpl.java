package org.rifidi.configuration.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.tree.DefaultConfigurationNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.rifidi.configuration.Configuration;
import org.rifidi.configuration.Constants;
import org.rifidi.configuration.ServiceFactory;
import org.rifidi.configuration.impl.DefaultConfigurationImpl;
import org.rifidi.configuration.listeners.AttributesChangedListener;
import org.rifidi.edge.core.services.notification.NotifierService;

/**
 * Implementation of service for managing configurations.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class ConfigurationServiceImpl implements ConfigurationService,
		AttributesChangedListener {
	/** Logger for this class */
	private static final Log logger = LogFactory
			.getLog(ConfigurationServiceImpl.class);
	/** Path to the configfile. */
	private final String path;
	/** Configurations. */
	private final ConcurrentHashMap<String, Set<Configuration>> factoryToConfigurations;
	/** Currently registered services. */
	private final Map<String, Configuration> IDToConfigurations;
	/** Service names that are already taken. */
	private final List<String> serviceNames;
	/** A notifier for JMS. Remove once we have aspects */
	private volatile NotifierService notifierService;
	/**
	 * Currently available factories by their names.
	 */
	private final Map<String, ServiceFactory<?>> factories;
	/** Contex for the bundle we are running in. */
	private final BundleContext context;

	/**
	 * Constructor.
	 */
	public ConfigurationServiceImpl(BundleContext context, String path, NotifierService notifierService) {
		this.notifierService=notifierService;
		this.path = path;
		this.context = context;
		factories = new HashMap<String, ServiceFactory<?>>();
		IDToConfigurations = new HashMap<String, Configuration>();
		serviceNames = new ArrayList<String>();
		factoryToConfigurations = loadConfig();
		logger.debug("ConfigurationServiceImpl instantiated.");
	}

	/**
	 * Load the configuration. Not thread safe.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, Set<Configuration>> loadConfig() {
		ConcurrentHashMap<String, Set<Configuration>> ret = new ConcurrentHashMap<String, Set<Configuration>>();
		try {
			HierarchicalINIConfiguration configuration = new HierarchicalINIConfiguration(
					path);
			// loop over configs
			for (Object sectionName : configuration.getSections()) {
				SubnodeConfiguration section = configuration
						.getSection((String) sectionName);
				String factoryName = section.getString(Constants.FACTORYID);
				// check if we have a type info
				if (factoryName == null) {
					logger.fatal("Missing factoryid attribute in config of "
							+ sectionName);
					continue;
				}
				final String serviceID = (String) sectionName;
				if (!checkName(serviceID)) {
					logger.fatal("service id " + serviceID
							+ " is invalid.  FactoryIDs must consist only of "
							+ "alphanumeric characters and the "
							+ "underscore character");
					continue;
				}
				if (ret.get(factoryName) == null) {
					ret.put(factoryName, new HashSet<Configuration>());
				}

				AttributeList attributes = new AttributeList();
				// get all properties
				Iterator<String> keys = section.getKeys();
				while (keys.hasNext()) {
					String key = keys.next();
					// factoryid is already processed
					if (Constants.FACTORYID.equals(key)) {
						continue;
					}
					// type is already processed
					if (Constants.FACTORY_TYPE.equals(key)) {
						continue;
					}
					attributes.add(new Attribute(key, section.getString(key)));
				}

				ret.get(factoryName).add(
						createAndRegisterConfiguration(serviceID, factoryName,
								attributes));
				serviceNames.add(serviceID);
				logger.debug("Read configuration for " + serviceID + " ("
						+ factoryName + ")");
			}
		} catch (ConfigurationException e) {
			logger.fatal("Can't open configuration: " + e);
		}
		return ret;
	}

	private Configuration createAndRegisterConfiguration(String serviceID,
			String factoryID, AttributeList attributes) {
		DefaultConfigurationImpl config = new DefaultConfigurationImpl(
				serviceID, factoryID, attributes, notifierService);
		config.setContext(context);
		IDToConfigurations.put(serviceID, config);

		String[] serviceInterfaces = new String[] { Configuration.class
				.getCanonicalName() };
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("serviceid", config.getServiceID());
		context.registerService(serviceInterfaces, config, params);

		try {
			context.addServiceListener(config, "(serviceid="
					+ config.getServiceID() + ")");
			logger.debug("Added listener for (serviceid="
					+ config.getServiceID() + ")");
		} catch (InvalidSyntaxException e) {
			logger.fatal(e);
		}
		return config;
	}

	/**
	 * Configuration names may only consist of alpha-numeric characters and the
	 * underscore character because of esper.
	 * 
	 * @param configurationName
	 *            The name of a configuration that is read in
	 * @return true if the configuration name passes the check. False otherwise.
	 */
	private boolean checkName(String configurationName) {
		String regex = "([A-Za-z0-9_])+";
		return configurationName.matches(regex);
	}

	/**
	 * The given service was bound to the registry.
	 * 
	 * @param serviceRef
	 * @throws Exception
	 */
	public void bind(ServiceFactory<?> factory, Map<?, ?> properties) {
		synchronized (factories) {
			for (String factoryID : factory.getFactoryIDs()) {
				if (factories.get(factoryID) == null) {
					logger.debug("Registering " + factoryID);
					factories.put(factoryID, factory);
					if (factoryToConfigurations.get(factoryID) != null) {
						for (Configuration serConf : factoryToConfigurations
								.get(factoryID)) {
							factory.createInstance(factoryID, serConf
									.getServiceID());
						}
					}
				}
			}
		}
	}

	/**
	 * The given service has been removed.
	 * 
	 * @param serviceRef
	 * @throws Exception
	 */
	public synchronized void unbind(ServiceFactory<?> factory,
			Map<?, ?> properties) {
		synchronized (factories) {
			for (String factoryID : factory.getFactoryIDs()) {
				logger.debug("Unregistering " + factoryID);
				factories.remove(factoryID);
			}
		}

	}

	@Override
	public void attributesChanged(String configurationID,
			AttributeList attributes) {
		Configuration config = IDToConfigurations.get(configurationID);
		// if (config != null) {
		// switch (config.getType()) {
		// case READER:
		// notifierService.attributesChanged(configurationID, attributes,
		// true);
		// break;
		// case COMMAND:
		// notifierService.attributesChanged(configurationID, attributes,
		// false);
		// break;
		// }
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.configuration.ConfigurationService#storeConfiguration()
	 */
	@Override
	public synchronized void storeConfiguration() {
		HashSet<Configuration> copy = new HashSet<Configuration>(
				IDToConfigurations.values());
		try {
			// TODO: copy file before deleting it!!
			File file = new File(path);
			file.delete();
			file.createNewFile();
			HierarchicalINIConfiguration configuration = new HierarchicalINIConfiguration(
					path);
			configuration.clear();
			for (Configuration config : copy) {
				DefaultConfigurationNode section = new DefaultConfigurationNode(
						config.getServiceID());
				DefaultConfigurationNode factoryid = new DefaultConfigurationNode(
						Constants.FACTORYID);
				factoryid.setValue(config.getFactoryID());
				section.addChild(factoryid);

				Map<String, Object> attrs = config.getAttributes();
				MBeanAttributeInfo[] infos = config.getMBeanInfo()
						.getAttributes();
				Map<String, MBeanAttributeInfo> attrInfo = new HashMap<String, MBeanAttributeInfo>();
				for (MBeanAttributeInfo i : infos) {
					attrInfo.put(i.getName(), i);
				}
				for (String key : attrs.keySet()) {
					if (attrInfo.get(key).isWritable()) {
						DefaultConfigurationNode newNode = new DefaultConfigurationNode(
								(String) key);
						newNode.setValue(attrs.get(key));
						section.addChild(newNode);
					}
				}
				configuration.getRootNode().addChild(section);
			}

			configuration.save();
		} catch (ConfigurationException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.configuration.services.ConfigurationService#createService(
	 * java.lang.String, javax.management.AttributeList)
	 */
	@Override
	public void createService(String factoryID, AttributeList attributes) {
		String serviceID = factoryID;
		serviceID = serviceID.replaceAll("[^A-Z^a-z^0-9^_]", "_") + "_";
		Integer counter = 1;
		String tempServiceID = serviceID + 1;
		// TODO: not nice but good enough for now
		while (serviceNames.contains(tempServiceID)) {
			counter++;
			tempServiceID = serviceID + counter;
		}
		serviceID = tempServiceID;

		// TODO: fix null factories
		ServiceFactory<?> factory = factories.get(factoryID);
		Configuration config = createAndRegisterConfiguration(serviceID,
				factoryID, attributes);

		factoryToConfigurations.get(factoryID).add(config);
		IDToConfigurations.put(serviceID, config);

		if (factory != null) {
			factory.createInstance(factoryID, serviceID);
		}
	}

	/**
	 * Set the current service factories.
	 * 
	 * @param serviceFactories
	 */
	public void setServiceFactories(Set<ServiceFactory<?>> serviceFactories) {
		for (ServiceFactory<?> serviceFactory : serviceFactories) {
			bind(serviceFactory, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.configuration.services.ConfigurationService#getConfiguration
	 * (java.lang.String)
	 */
	@Override
	public Configuration getConfiguration(String serviceID) {
		return IDToConfigurations.get(serviceID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.configuration.services.ConfigurationService#getConfigurations
	 * ()
	 */
	@Override
	public Set<Configuration> getConfigurations() {
		return new HashSet<Configuration>(IDToConfigurations.values());
	}

}
