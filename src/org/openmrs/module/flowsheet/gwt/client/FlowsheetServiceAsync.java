package org.openmrs.module.flowsheet.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowsheetServiceAsync {
	void getObsData(String patientId, AsyncCallback<String> callback);
}
