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
package org.rifidi.edge.adapter.awid.commands.awid2010;

import javax.management.MBeanInfo;

import org.rifidi.edge.configuration.AnnotationMBeanInfoStrategy;
import org.rifidi.edge.configuration.JMXMBean;
import org.rifidi.edge.configuration.Property;
import org.rifidi.edge.configuration.PropertyType;
import org.rifidi.edge.sensors.AbstractCommandConfiguration;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Daniel G�mez - dgomez@idlinksolutions.com
 * 
 */
@JMXMBean
public class AwidPortalIDWithMaskCommandConfiguration extends
		AbstractCommandConfiguration<AwidPortalIDCommand> {
	// 0F 20 5E 02 04 01 20 06 FC 01 08 00 00 XX XX
	private byte packetLength = 0x0F;
	private byte memoryBank = 0x01;
	private byte startingBit = 0x20;
	private byte maskLength = 0x01;
	private byte maskValue = 0x0E;
	private byte qValue = 0x02;
	private byte timeout = 0x00;
	private byte repeat = 0x00;

	public static final MBeanInfo mbeaninfo;
	static {
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy
				.getMBeanInfo(AwidPortalIDWithMaskCommandConfiguration.class);
	}

	@Override
	public AwidPortalIDCommand getCommand(String readerID) {
		return new AwidPortalIDCommand(super.getID(), packetLength,
				memoryBank, startingBit, maskLength, maskValue, qValue,
				timeout, repeat);
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return (MBeanInfo) mbeaninfo.clone();
	}

	/**
	 * @return the packetLength
	 */
	@Property(defaultValue = "15", description = "1-byte packet length, value depending on how long the mask is or simply MaskLength plus fourteen", displayName = "Packet Length", orderValue = 1, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public byte getPacketLength() {
		return packetLength;
	}

	/**
	 * @param packetLength
	 *            the packetLength to set
	 */
	public void setPacketLength(Integer packetLength) {
		// TODO: bounds check
		this.packetLength = packetLength.byteValue();
	}

	/**
	 * @return the memoryBank
	 */
	@Property(defaultValue = "02", description = "0x01 EPC bank, 0x02 TID bank, 0x03 User bank", displayName = "Memory Bank", orderValue = 2, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getMemoryBank() {
		return new Integer(memoryBank);
	}

	/**
	 * @param memoryBank
	 *            the memoryBank to set
	 */
	public void setMemoryBank(Integer memoryBank) {
		// TODO: bounds check
		this.memoryBank = memoryBank.byteValue();
	}

	/**
	 * @return the startingBit
	 */
	@Property(defaultValue = "00", description = "Starting bit position in memory bank, starting default for EPC Bank", displayName = "Starting Bit", orderValue = 3, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getStartingBit() {
		return new Integer(startingBit);
	}

	/**
	 * @param startingBit
	 *            the startingBit to set
	 */
	public void setStartingBit(Integer startingBit) {
		// TODO: Bounds Check
		this.startingBit = startingBit.byteValue();
	}

	/**
	 * @return the maskLength
	 */
	@Property(defaultValue = "04", description = "Mask data length.", displayName = "Mask Length", orderValue = 4, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getMaskLength() {
		return new Integer(maskLength);
	}

	/**
	 * @param maskLength
	 *            the maskLength to set
	 */
	public void setMaskLength(Integer maskLength) {
		// TODO: bounds check
		this.maskLength = maskLength.byteValue();
	}

	/**
	 * @return the maskValue
	 */
	@Property(defaultValue = "14", description = "Mask value for data. Example 0x00 is for read all data", displayName = "Mask Value", orderValue = 5, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getMaskValue() {
		return new Integer(maskValue);
	}

	/**
	 * @param maskValue
	 *            the maskValue to set
	 */
	public void setMaskValue(Integer maskValue) {
		// TODO: bounds check
		this.maskValue = maskValue.byteValue();
	}

	/**
	 * @return the qValue
	 */
	@Property(defaultValue = "02", description = "The formula is #Tag=2^QValue-1", displayName = "QValue", orderValue = 6, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getqValue() {
		return new Integer(qValue);
	}

	/**
	 * @param qValue
	 *            the qValue to set
	 */
	public void setqValue(Integer qValue) {
		// TODO: bounds check
		this.qValue = qValue.byteValue();
	}

	/**
	 * @return the timeout
	 */
	@Property(defaultValue = "00", description = "Execute this command for timeout*100 ms. If set to 0x00, execute until stop command is sent", displayName = "Timeout", orderValue = 7, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getTimeout() {
		return new Integer(timeout);
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(Integer timeout) {
		// TODO: bounds check
		this.timeout = timeout.byteValue();
	}

	/**
	 * @return the repeat
	 */
	@Property(defaultValue = "00", description = " Return results every repeat*100 ms. If set to 0x00 continuously return tags.", displayName = "Repeat", orderValue = 8, type = PropertyType.PT_INTEGER, writable = true, minValue = "0", maxValue = "255")
	public Integer getRepeat() {
		return new Integer(repeat);
	}

	/**
	 * @param repeat
	 *            the repeat to set
	 */
	public void setRepeat(Integer repeat) {
		// TODO bounds check
		this.repeat = repeat.byteValue();
	}

}
