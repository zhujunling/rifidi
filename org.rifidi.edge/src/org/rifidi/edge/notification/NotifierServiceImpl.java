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
package org.rifidi.edge.notification;

import javax.jms.Destination;
import javax.management.AttributeList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.edge.api.CommandConfigFactoryAdded;
import org.rifidi.edge.api.CommandConfigFactoryRemoved;
import org.rifidi.edge.api.CommandConfigurationAddedNotification;
import org.rifidi.edge.api.CommandConfigurationRemovedNotification;
import org.rifidi.edge.api.JobDeletedNotification;
import org.rifidi.edge.api.JobSubmittedNotification;
import org.rifidi.edge.api.PropertyChangedNotification;
import org.rifidi.edge.api.ReaderAddedNotification;
import org.rifidi.edge.api.ReaderFactoryAddedNotification;
import org.rifidi.edge.api.ReaderFactoryRemovedNotification;
import org.rifidi.edge.api.ReaderRemovedNotification;
import org.rifidi.edge.api.SessionAddedNotification;
import org.rifidi.edge.api.SessionRemovedNotification;
import org.rifidi.edge.api.SessionStatus;
import org.rifidi.edge.api.SessionStatusChangedNotification;
import org.springframework.jms.core.JmsTemplate;

/**
 * This object is instantiated by spring and listens for certain events to
 * happen (such as Readers added to the edge server), and then sends out JMS
 * notifications
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class NotifierServiceImpl implements NotifierService {

	/** The template for sending out Notification messages */
	private JmsTemplate extNotificationTemplate;
	/** The queue to send out notifications on */
	private Destination extNotificationDest;
	/**The queue to send out tags on*/
	private Destination externalTagsDest;
	/** The logger for this class */
	private Log logger = LogFactory.getLog(NotifierServiceImpl.class);

	/**
	 * Called by Spring
	 * 
	 * @param exextNotificationTemplate
	 *            the exextNotificationQueue to set
	 */
	public void setExtNotificationTemplate(JmsTemplate extNotificationTemplate) {
		this.extNotificationTemplate = extNotificationTemplate;
	}

	/**
	 * called by Spring
	 * 
	 * @param extNotificationDest
	 *            the extNotificationDest to set
	 */
	public void setExtNotificationDest(Destination extNotificationDest) {
		this.extNotificationDest = extNotificationDest;
	}
	
	/**
	 * called by Spring
	 * 
	 * @param extNotificationDest
	 *            the extNotificationDest to set
	 */
	public void setExtTagsDest(Destination extTagsDest) {
		this.externalTagsDest = extTagsDest;
	}

	@Override
	public void addCommandEvent(String commandID) {
		try {
			extNotificationTemplate
					.send(this.extNotificationDest,
							new NotificationMessageCreator(
									new CommandConfigurationAddedNotification(
											commandID)));
		} catch (Exception e) {
			logger.warn("commandconfiguraitonAdded Notification not sent ");
		}

	}

	@Override
	public void addReaderEvent(String readerID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(new ReaderAddedNotification(
							readerID)));
		} catch (Exception e) {
			logger.warn("ReaderAddNotification not sent");
		}

	}

	@Override
	public void removeCommandEvent(String commandID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new CommandConfigurationRemovedNotification(
									commandID)));
		} catch (Exception e) {
			logger.warn("CommandConfiguraitonRemoved Notification not sent ");
		}

	}

	@Override
	public void removeReaderEvent(String readerID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new ReaderRemovedNotification(readerID)));
		} catch (Exception e) {
			logger.warn("Remove Reader Event not sent ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.notifications.NotifierService#addSessionEvent(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void addSessionEvent(String readerID, String sessionID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new SessionAddedNotification(readerID, sessionID)));
		} catch (Exception e) {
			logger.warn("Session Added Notification not sent: ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.notifications.NotifierService#removeSessionEvent
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeSessionEvent(String readerID, String sessionID) {
		try {
			extNotificationTemplate
					.send(this.extNotificationDest,
							new NotificationMessageCreator(
									new SessionRemovedNotification(readerID,
											sessionID)));
		} catch (Exception e) {
			logger.warn("Session Removed Notification not sent: ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.edge.notifications.NotifierService#addReaderFactoryEvent
	 * (java.lang.String)
	 */
	@Override
	public void addReaderFactoryEvent(String readerFactoryID) {
		try {
			extNotificationTemplate
					.send(this.extNotificationDest,
							new NotificationMessageCreator(
									new ReaderFactoryAddedNotification(
											readerFactoryID)));
		} catch (Exception e) {
			logger.warn("ReaderFactoryAdded Notification not sent ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.notifications.NotifierService#removeReaderFactoryEvent
	 * (java.lang.String)
	 */
	@Override
	public void removeReaderFactoryEvent(String readerFactoryID) {
		try{
		extNotificationTemplate.send(this.extNotificationDest,
				new NotificationMessageCreator(
						new ReaderFactoryRemovedNotification(readerFactoryID)));
		}catch(Exception e){
			logger.warn("ReaderFactoryRemoved Notification not sent ");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.notifications.NotifierService#sessionStatusChanged
	 * (java.lang.String, java.lang.String,
	 * org.rifidi.edge.core.api.SessionStatus)
	 */
	@Override
	public void sessionStatusChanged(String readerID, String sessionID,
			SessionStatus sessionStatus) {
		try{
		extNotificationTemplate.send(this.extNotificationDest,
				new NotificationMessageCreator(
						new SessionStatusChangedNotification(readerID,
								sessionID, sessionStatus)));
		}catch(Exception e){
			logger.warn("SessionStatusChanged Notification not sent");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.notifications.NotifierService#
	 * addCommandConfigFactoryEvent(java.lang.String, java.util.Set)
	 */
	@Override
	public void addCommandConfigFactoryEvent(String readerFactoryID,
			String commandConfigFactoryID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new CommandConfigFactoryAdded(readerFactoryID,
									commandConfigFactoryID)));
		} catch (Exception e) {
			logger.warn("ReaderFactoryAdded Notification not sent ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.edge.core.notifications.NotifierService#
	 * removeCommandConfigFactoryEvent(java.lang.String)
	 */
	@Override
	public void removeCommandConfigFactoryEvent(String readerFactoryID,
			String commandFactoryID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new CommandConfigFactoryRemoved(readerFactoryID,
									commandFactoryID)));
		} catch (Exception e) {
			logger.warn("ReaderFactoryRemoved Notification not sent ");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.notifications.NotifierService#jobDeleted(java.lang
	 * .String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public void jobDeleted(String readerID, String sessionID, Integer jobID) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(new JobDeletedNotification(
							readerID, sessionID, jobID)));
		} catch (Exception e) {
			logger.warn("job deleted Notification not sent ");

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.core.notifications.NotifierService#jobSubmitted(java.
	 * lang.String, java.lang.String, java.lang.Integer, java.lang.String)
	 */
	@Override
	public void jobSubmitted(String readerID, String sessionID, Integer jobID,
			String commandID, boolean recurring) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new JobSubmittedNotification(readerID, sessionID,
									jobID, commandID, recurring)));
		} catch (Exception e) {
			logger.warn("job submitted Notification not sent ");

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.edge.notification.NotifierService#attributesChanged
	 * (java.lang.String, javax.management.AttributeList)
	 */
	@Override
	public void attributesChanged(String configurationID,
			AttributeList attributes) {
		try {
			extNotificationTemplate.send(this.extNotificationDest,
					new NotificationMessageCreator(
							new PropertyChangedNotification(configurationID,
									attributes)));
		} catch (Exception e) {
			logger.warn("property changed Notification not sent ");

		}

	}

	/* (non-Javadoc)
	 * @see org.rifidi.edge.notification.NotifierService#tagSeen(org.rifidi.edge.notification.data.ReadCycle)
	 */
	@Override
	public void tagSeen(ReadCycle cycle) {
		extNotificationTemplate.send(externalTagsDest,
				new TagMessageMessageCreator(cycle.getBatch()));
		
	}
	
}
