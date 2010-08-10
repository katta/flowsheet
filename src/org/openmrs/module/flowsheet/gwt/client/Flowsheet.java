package org.openmrs.module.flowsheet.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point class to the Flowsheet module
 * Renders the UI fot the Flowsheet module
 * 
 * @author umashanthi
 *
 */

public class Flowsheet implements EntryPoint{

	@Override
	public void onModuleLoad() {
		// Retrieves patientId from request parameter
		String patientId = com.google.gwt.user.client.Window.Location
		.getParameter("patientId");
		// Generates the UI 
		RootPanel.get().add(new FlowsheetPanel(patientId));
	}
	
}


