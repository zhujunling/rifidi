/*******************************************************************************
 * Copyright (c) 2014 Transcends, LLC.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version. This program is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You 
 * should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, 
 * USA. 
 * http://www.gnu.org/licenses/gpl-2.0.html
 *******************************************************************************/
/**
 * 
 */
package org.rifidi.edge.adapter.alien.autonomous;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.adapter.alien.AlienMessageParsingStrategyFactory;
import org.rifidi.edge.api.SessionStatus;
import org.rifidi.edge.notification.NotifierService;
import org.rifidi.edge.sensors.AbstractCommandConfiguration;
import org.rifidi.edge.sensors.AbstractSensor;
import org.rifidi.edge.sensors.sessions.AbstractServerSocketSensorSession;
import org.rifidi.edge.sensors.sessions.MessageParsingStrategyFactory;
import org.rifidi.edge.sensors.sessions.MessageProcessingStrategyFactory;

/**
 * The Session that Alien Readers can send reports to.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AlienAutonomousSensorSession extends
		AbstractServerSocketSensorSession {

	/** The logger */
	@SuppressWarnings("unused")
	private final static Log logger = LogFactory
			.getLog(AlienAutonomousSensorSession.class);
	/** The notifierService used to send out notifications of session changes */
	private NotifierService notifierService;
	/** The factory that produces Alien Message Parsing Strategy */
	private AlienMessageParsingStrategyFactory messageParserFactory;
	/** The factory that produces Alien Autonomous Message Processing Strategies */
	private AlienAutonomousMessageProcessingStrategyFactory messageProcessingFactory;

	/**
	 * 
	 * @param sensor
	 * @param ID
	 * @param notifierService
	 * @param serverSocketPort
	 * @param maxNumAutonomousReaders
	 * @param commands
	 */
	public AlienAutonomousSensorSession(AbstractSensor<?> sensor, String ID,
			NotifierService notifierService, int serverSocketPort,
			int maxNumAutonomousReaders,
			Set<AbstractCommandConfiguration<?>> commands) {
		super(sensor, ID, serverSocketPort, maxNumAutonomousReaders, commands);
		this.notifierService = notifierService;
		this.messageParserFactory = new AlienMessageParsingStrategyFactory();
		this.messageProcessingFactory = new AlienAutonomousMessageProcessingStrategyFactory(
				this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.sensors.base.AbstractServerSocketSensorSession#
	 * getMessageParsingStrategyFactory()
	 */
	@Override
	protected MessageParsingStrategyFactory getMessageParsingStrategyFactory() {
		return this.messageParserFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.sensors.base.AbstractServerSocketSensorSession#
	 * getMessageProcessingStrategyFactory()
	 */
	@Override
	protected MessageProcessingStrategyFactory getMessageProcessingStrategyFactory() {
		return this.messageProcessingFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.sensors.base.AbstractSensorSession#setStatus(org
	 * .rifidi.edge.api.SessionStatus)
	 */
	@Override
	protected synchronized void setStatus(SessionStatus status) {
		super.setStatus(status);
		notifierService.sessionStatusChanged(super.getSensor().getID(),
				getID(), status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.sensors.sessions.AbstractServerSocketSensorSession
	 * #toString()
	 */
	@Override
	public String toString() {
		return "[Autonomous Session " + super.toString() + "]";
	}

}
