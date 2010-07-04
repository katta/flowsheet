package org.openmrs.module.flowsheet.gwt.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.module.flowsheet.gwt.client.model.UINumericData;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../../../moduleServlet/flowsheet/flowsheetService")
public interface FlowsheetService extends RemoteService {
	// PatientObsCollection[] getObsData(String patientId);
	List<UIObs> getPatientObsData(String patientId, Date startDate,
			Date endDate, Set<Integer> conceptIds);

	Date[] getDateRange(String patiendId);

	Map<Integer, String> getConceptList(String patientId);

	List<UIObs> getDetailedData(String patientId, Date date, Integer conceptId);

	UINumericData[] getNumericValueHistory(String patientId, Integer conceptId,
			Date startDate, Date endDate);
	String[] getPatientObsDetails(String patientId, Date date,Integer conceptId);

	String[] getDataForNumericValueHistory(Integer conceptId);
	String[][] getConceptClassList(String patientId,Date startDate,Date endDate);
}
