/**
 * 
 */
package com.csc.rfid.toolcrib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.rifidi.edge.core.services.esper.EsperManagementService;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

/**
 * This application will monitor the incoming and outgoing parts through a window for CSC/UTC.  The 
 *
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class ToolcribApp {
	/** Esper service */
	private volatile EsperManagementService esperService;
	/** All statements that have been defined so far */
	private final Set<EPStatement> statements = new CopyOnWriteArraySet<EPStatement>();

	public ToolcribApp() {
		Activator.myApp = this;
	}
	
	/**
	 * Start the application. This method submits the esper statements to the
	 * esper runtime.
	 * 
	 * This method is called by spring
	 */
	public void start() {
		// esper statement that creates a window.
		// TODO: Add velocity information to be stored in this window
		statements
				.add(esperService.getProvider().getEPAdministrator().createEPL(
						"create window tags.win:keepall()" + "(tag_ID String, speed Float)"));

		// esper statement taht adds information to the window
		// TODO make sure that velocity info is inserted into the window
		statements
				.add(esperService
						.getProvider()
						.getEPAdministrator()
						.createEPL(
								"on ReadCycle[select * from tags]"
										+ "insert into tags select cast(tag.epc?, String) as tag_ID , "
										+ "cast(extraInformation('Speed'),Float) as speed"));

		// esper statement that removes all tags with a given ID from the window
		// if the tag has not been seen at the antenna in the last 60 seconds
		statements.add(esperService.getProvider().getEPAdministrator()
				.createEPL(
						"on pattern [every tag=tags ->"
								+ "(timer:interval("+System.getProperty("org.rifidi.time")+" sec) and not tags"
								+ "(tag_ID = tag.tag_ID))]"
								+ "delete from tags where "
								+ "tag_ID = tag.tag_ID"));

		// esper statement that listens to add and remove events from the window
		EPStatement queryAllTags = esperService.getProvider()
				.getEPAdministrator().createEPL("select irstream * from tags");

		// add a listener to the above statement
		queryAllTags.addListener(new StatementAwareUpdateListener() {

			@Override
			public void update(EventBean[] arg0, EventBean[] arg1,
					EPStatement arg2, EPServiceProvider arg3) {

				// Map that will hold the tag ID bound to all the speed
				// information that is retrieved.
				HashMap<String, ArrayList<Float>> speedMap = new HashMap<String, ArrayList<Float>>();

				// arg1 contains all the tag reads. This works because we do all
				// of the rmoves at once.
				if (arg1 != null) {
					for (EventBean b : arg1) {
						String epc = (String) b.get("tag_ID");
						Float speed = (Float) b.get("speed");
						if (!speedMap.containsKey(epc)) {
							speedMap.put(epc, new ArrayList<Float>());
						}
						speedMap.get(epc).add(speed);
					}
					System.out.println(".");
				}
				for(String key:speedMap.keySet()) {
					Float speed = getSpeed(speedMap.get(key));
					System.out.println("Tag is: " + key + ", Speed is: " + speed);
				}
			}
		});
		statements.add(queryAllTags);
	}

	/**
	 * We will look at the relative speeds of the object, and we will add up the
	 * velocity values to see which way the box is going.
	 * 
	 * @param speed
	 * @return
	 */
	private Float getSpeed(ArrayList<Float> speed) {
		float retVal = 0.0f;

		for(Float f:speed) {
			retVal+=f;
		}
		
		return retVal;
	}

	/**
	 * Called by spring Iterate through all statements and stop them.
	 */
	public void stop() {
		for (EPStatement statement : statements) {
			statement.destroy();
		}
	}

	/**
	 * Called by spring
	 * 
	 * @param esperService
	 *            the esperService to set
	 */
	public void setEsperService(EsperManagementService esperService) {
		this.esperService = esperService;
		start();
	}
}
