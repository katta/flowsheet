package org.openmrs.module.flowsheet.extension.html;

import org.openmrs.module.web.extension.PatientDashboardTabExt;
/**
 * Responsible to adding a tab to the Patient Dashboard 
 * 
 * @author umashanthi
 *
 */

public class FlowsheetTabExt extends PatientDashboardTabExt {
	
	@Override
	public String getPortletUrl() {
		return "flowsheetForm";
	}

	@Override
	public String getRequiredPrivilege() {
		return "View Patients";
	}

	@Override
	public String getTabId() {

		return "flowsheet";
	}

	@Override
	public String getTabName() {

		return "Flowsheet";
	}

}
