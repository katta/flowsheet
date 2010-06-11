package org.openmrs.module.flowsheet.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowsheetServiceAsync {
	public void getObsData(String patientId, AsyncCallback<PatientObsCollection[]> callback);
}
