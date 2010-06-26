package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowsheetServiceAsync {
	// public void getObsData(String patientId,
	// AsyncCallback<PatientObsCollection[]> callback);
	public void getPatientObsData(String patientId, Date startDate,
			Date endDate, List<Integer> conceptIds,AsyncCallback<List<UIObs>> callback);

	public void getDateRange(String patientId, AsyncCallback<Date[]> callback);
	public void getConceptList(String patientId,AsyncCallback<Map<Integer,String>> callback); 
}
