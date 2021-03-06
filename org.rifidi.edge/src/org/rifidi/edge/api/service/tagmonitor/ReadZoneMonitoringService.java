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
package org.rifidi.edge.api.service.tagmonitor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Read Zone Monitoring Service notifies subscribers when tags have arrived
 * at and departed from a read zone.
 * 
 * By default, departure happens if a tag has not been seen in a read zone for
 * two seconds.
 * 
 * @author Matthew Dean
 * @author Kyle Neumeier - kyle@pramari.com
 */
public interface ReadZoneMonitoringService {

	/**
	 * Subscribe to arrival and departure events from all readers and antennas.
	 * 
	 * @param subscriber
	 *            The subscriber
	 */
	public void subscribe(ReadZoneSubscriber subscriber);

	/**
	 * Subscribe to arrival and departure events from the given read zones
	 * 
	 * @param subscriber
	 *            The subscriber
	 * @param readZones
	 *            The readzones to monitor. If the set is empty, monitor all
	 *            readers and antennas.
	 * @param departureTime
	 *            If this amount of time passes since the last time a tag has
	 *            been seen, then fire a departure event.
	 * @param timeUnit
	 *            The unit used for the departure time.
	 */
	public void subscribe(ReadZoneSubscriber subscriber,
			List<ReadZone> readZones, Float departureTime, TimeUnit timeUnit);
	
	/**
	 * Subscribe to arrival and departure events from the given read zones
	 * 
	 * @param subscriber
	 *            The subscriber
	 * @param readZones
	 *            The readzones to monitor. If the set is empty, monitor all
	 *            readers and antennas.
	 * @param departureTime
	 *            If this amount of time passes since the last time a tag has
	 *            been seen, then fire a departure event.
	 * @param timeUnit
	 *            The unit used for the departure time.
	 * @param wholereader
	 *            If this value is set to "true", the subscriber will not
	 *            receive new arrival events if the tag switches between
	 *            antennas or readers that are included in the given readzones.
	 *            For instance, if the reader LLRP_1 antennas 1 and 2 are being
	 *            monitored, and the tag disappears on antenna 1 and reappears
	 *            on antenna 2, a new arrival event will not occur for the
	 *            switch to antenna 2.
	 */
	public void subscribe(ReadZoneSubscriber subscriber,
			List<ReadZone> readZones, Float departureTime, TimeUnit timeUnit,
			boolean wholereader);

	/**
	 * Subscribe to arrival and departure events from the given read zones
	 * 
	 * @param subscriber
	 *            The subscriber
	 * @param readZones
	 *            The readzones to monitor. If the set is empty, monitor all
	 *            readers and antennas.
	 * @param departureTime
	 *            If this amount of time passes since the last time a tag has
	 *            been seen, then fire a departure event.
	 * @param timeUnit
	 *            The unit used for the departure time.
	 * @param wholereader
	 *            If this value is set to "true", the subscriber will not
	 *            receive new arrival events if the tag switches between
	 *            antennas or readers that are included in the given readzones.
	 *            For instance, if the reader LLRP_1 antennas 1 and 2 are being
	 *            monitored, and the tag disappears on antenna 1 and reappears
	 *            on antenna 2, a new arrival event will not occur for the
	 *            switch to antenna 2.
	 * @param useRegex
	 * 		      Are you using regular expressions in the readezones you pass in?  
	 */
	public void subscribe(ReadZoneSubscriber subscriber,
			List<ReadZone> readZones, Float departureTime, TimeUnit timeUnit,
			boolean wholereader, boolean useRegex);

	/**
	 * Subscribe to the arrival and departure events for the given read zone.
	 * 
	 * @param subscriber
	 * @param readZone
	 * @param departureTime
	 * @param timeUnit
	 */
	public void subscribe(ReadZoneSubscriber subscriber, ReadZone readZone,
			Float departureTime, TimeUnit timeUnit);

	/**
	 * Unsubscribe from arrival and departure events
	 * 
	 * @param subscriber
	 * @return
	 */
	public void unsubscribe(ReadZoneSubscriber subscriber);

}
