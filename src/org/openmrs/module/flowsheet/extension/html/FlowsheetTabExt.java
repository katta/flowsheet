package org.openmrs.module.flowsheet.extension.html;

import org.openmrs.module.web.extension.PatientDashboardTabExt;

public class FlowsheetTabExt extends PatientDashboardTabExt {
	// TODO: add string constants to message properties -u
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
