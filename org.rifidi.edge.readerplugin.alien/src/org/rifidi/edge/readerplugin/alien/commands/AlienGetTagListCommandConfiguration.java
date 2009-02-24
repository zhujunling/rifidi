/**
 * 
 */
package org.rifidi.edge.readerplugin.alien.commands;

import org.rifidi.configuration.annotations.JMXMBean;
import org.rifidi.configuration.annotations.Property;
import org.rifidi.edge.core.commands.AbstractCommandConfiguration;

/**
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
@JMXMBean
public class AlienGetTagListCommandConfiguration extends
		AbstractCommandConfiguration<AlienGetTagListCommand> {
	/** Interval between two reads. */
	private Integer interval = 10;
	/** Name of the command. */
	private static final String name = "Alien9800-GetTagList";
	/** Description of the command. */
	private static final String description = "Get list of tags that appeared in the antenna field since the last call.";
	/** Unique id for this command. */
	private String id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.commands.AbstractCommandConfiguration#getCommandDescription
	 * ()
	 */
	@Override
	public String getCommandDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.commands.AbstractCommandConfiguration#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return name;
	}

	/**
	 * @return the interval
	 */
	@Property(name = "Interval", description = "Interval between two reads", writable = true)
	public String getInterval() {
		return Integer.toString(interval);
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(String interval) {
		this.interval = Integer.parseInt(interval);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.core.commands.AbstractCommandConfiguration#getCommand()
	 */
	@Override
	public AlienGetTagListCommand getCommand() {
		return new AlienGetTagListCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.configuration.RifidiService#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.configuration.RifidiService#setID(java.lang.String)
	 */
	@Override
	public void setID(String id) {
		this.id = id;
	}

}