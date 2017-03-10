/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.slee.resource.statistics;


import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.InvalidArgumentException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;

//import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;
//import com.codahale.metrics.Counter;
//import com.codahale.metrics.MetricRegistry;

import java.util.TimerTask;

/**
 *
 */
public class StatisticsResourceAdaptor implements ResourceAdaptor {
	private transient Tracer tracer;

	private ResourceAdaptorContext raContext;
	private SleeEndpoint sleeEndpoint;

	private SleeContainer sleeContainer;
	private ResourceManagement resourceManagement;

	/**
	 * the EventLookupFacility is used to look up the event id of incoming
	 * events
	 */
	private EventLookupFacility eventLookup;


    private volatile boolean shutdownServer = false;

	public StatisticsResourceAdaptor() { }

	public ResourceAdaptorContext getResourceAdaptorContext() {
		return raContext;
	}

	public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}

	// Restcomm Statistics
	/*
	protected static final String STATISTICS_SERVER = "statistics.server";
	protected static final String DEFAULT_STATISTICS_SERVER = "https://statistics.restcomm.com/rest/";

	private RestcommStatsReporter statsReporter = new RestcommStatsReporter();
	private MetricRegistry metrics = RestcommStatsReporter.getMetricRegistry();
	// define metric name
	private Counter counterCalls = metrics.counter("calls");
	private Counter counterSeconds = metrics.counter("seconds");
	private Counter counterMessages = metrics.counter("messages");
	*/

	// lifecycle methods

	public void setResourceAdaptorContext(ResourceAdaptorContext ctxt) {
		raContext = ctxt;
		tracer = ctxt.getTracer(StatisticsResourceAdaptor.class.getSimpleName());

		sleeEndpoint = ctxt.getSleeEndpoint();
		eventLookup = ctxt.getEventLookupFacility();

		sleeContainer = SleeContainer.lookupFromJndi();
		System.out.println("sleeContainer: " + sleeContainer);
		if (sleeContainer != null) {
			resourceManagement = sleeContainer.getResourceManagement();
			System.out.println("resourceManagement: " + resourceManagement);

			//for (String raEntity: resourceManagement.getResourceAdaptorEntities()) {
			//	tracer.info("RA Entity: " + raEntity);
			//	System.out.println("RA Entity: " + raEntity);
			//}
		}

		ctxt.getTimer().schedule(new StatisticsTimerTask(), 5000, 5000);
	}

	private class StatisticsTimerTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("MyTimerTask run()");

			if (resourceManagement != null) {
				for (String raEntity: resourceManagement.getResourceAdaptorEntities()) {
					tracer.info("RA Entity: " + raEntity);
					System.out.println("RA Entity: " + raEntity);

					try {
						ObjectName usageMBeanName = resourceManagement.getResourceUsageMBean(raEntity);
						System.out.println("RA UsageMBean: " + usageMBeanName);

					} catch (UnrecognizedResourceAdaptorEntityException e) {
						System.out.println("RA UsageMBean is not exists");
					} catch (InvalidArgumentException e) {
						System.out.println("RA UsageMBean is not exists");
					} catch (Exception e) {
						// TODO
						System.out.println("RA UsageMBean is not exists");
					}
				}
			}
		}
	}

	public void raConfigure(ConfigProperties properties) {
	}

	public void raActive() {

		// Restcomm Statistics
		/*
		if (statsReporter==null)
			statsReporter = new RestcommStatsReporter();
		String statisticsServer = null; // Version.getVersionProperty(STATISTICS_SERVER);
		if (statisticsServer == null || !statisticsServer.contains("http")) {
			statisticsServer = DEFAULT_STATISTICS_SERVER;
		}
		//define remote server address (optionally)
		statsReporter.setRemoteServer(statisticsServer);
		String projectName = System.getProperty("RestcommProjectName", "jainslee");
		String projectType = System.getProperty("RestcommProjectType", "community");
		String projectVersion = System.getProperty("RestcommProjectVersion", "1234");
		//		Version.getVersionProperty(Version.RELEASE_VERSION));
		if (tracer.isFineEnabled()) {
			tracer.fine("Restcomm Stats " + projectName + " " + projectType + " " + projectVersion);
		}
		statsReporter.setProjectName(projectName);
		statsReporter.setProjectType(projectType);
		statsReporter.setVersion(projectVersion);
		//define periodicy - default to once a day
		//statsReporter.start(86400, TimeUnit.SECONDS);

		//Version.printVersion();
		*/

	}

	public void raStopping() { }

	public void raInactive() {
		//statsReporter.stop();
		//statsReporter = null;
	}

	public void raUnconfigure() { }

	public void unsetResourceAdaptorContext() {
		raContext = null;
		tracer = null;
		sleeEndpoint = null;
		eventLookup = null;

		sleeContainer = null;
		resourceManagement = null;
	}

	// config management methods
	public void raVerifyConfiguration(ConfigProperties properties)
			throws javax.slee.resource.InvalidConfigurationException {
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		throw new UnsupportedOperationException();
	}

	// event filtering methods


	public void serviceActive(ReceivableService service) {
	}

	public void serviceStopping(ReceivableService service) {
	}

	public void serviceInactive(ReceivableService service) {
	}


	// mandatory callbacks

	public void administrativeRemove(ActivityHandle handle) {
	}


	public Object getActivity(ActivityHandle activityHandle) {
		return null;
	}

	public ActivityHandle getActivityHandle(Object activity) {
		return null;
	}

	// optional call-backs
	public void activityEnded(ActivityHandle handle) {

	}

	public void activityUnreferenced(ActivityHandle activityHandle) {

	}

	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {

	}

	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object event, Address arg3, ReceivableService arg4, int arg5) {

	}

	public void queryLiveness(ActivityHandle activityHandle) {
	}

	// interface accessors

	public Object getResourceAdaptorInterface(String arg0) {
		return null;
	}

	public Marshaler getMarshaler() {
		return null;
	}

	// ra logic
}
