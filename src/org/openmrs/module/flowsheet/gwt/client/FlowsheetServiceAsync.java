package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.module.flowsheet.gwt.client.model.UINumericData;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowsheetServiceAsync {
	// public void getObsData(String patientId,
	// AsyncCallback<PatientObsCollection[]> callback);
	public void getPatientObsData(String patientId, Date startDate,
			Date endDate, Set<Integer> conceptIds,
			AsyncCallback<List<UIObs>> callback);

	public void getDateRange(String patientId, AsyncCallback<Date[]> callback);

	public void getConceptList(String patientId,
			AsyncCallback<Map<Integer, String>> callback);

	public void getDetailedData(String patientId, Date date, Integer conceptId,
			AsyncCallback<List<UIObs>> callback);

	public void getDataForNumericValueHistory(Integer conceptId,
			AsyncCallback<String[]> callback);

	public void getNumericValueHistory(String patientId, Integer conceptId,
			Date startDate, Date endDate,
			AsyncCallback<UINumericData[]> callback);

	public void getPatientObsDetails(String patientId, Date date,
			Integer conceptId, AsyncCallback<String[]> callback);

	public void getConceptClassList(String patientId, Date startDate,
			Date endDate, AsyncCallback<String[][]> callback);
}
