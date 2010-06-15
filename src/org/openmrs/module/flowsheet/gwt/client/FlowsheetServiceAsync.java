package org.openmrs.module.flowsheet.gwt.client;

import java.util.List;

import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowsheetServiceAsync {
	public void getObsData(String patientId, AsyncCallback<PatientObsCollection[]> callback);
    public void getPatientObsData(String patientId,AsyncCallback<List<UIObs> > callback);
}
