/**
 * 
 */
package org.rifidi.configuration;

import java.util.Map;

import javax.management.DynamicMBean;

/**
 * Configurations provide a standard interface for handling services through a
 * configuration/management interface.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public interface Configuration extends DynamicMBean {

	/**
	 * Get the unique name of the configuration.
	 * 
	 * @return
	 */
	String getServiceID();

	/**
	 * Get the id of the factory that registered the configuration.
	 * 
	 * @return
	 */
	String getFactoryID();

	/**
	 * Used for persistence.
	 */
	Map<String, String> getAttributes();

	/**
	 * Destroy the service and remove it from the registry.
	 */
	void destroy();
}