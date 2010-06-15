package org.openmrs.module.flowsheet.gwt.client;

import java.util.List;

import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../../../moduleServlet/flowsheet/flowsheetService")
public interface FlowsheetService extends RemoteService{
	PatientObsCollection[] getObsData(String patientId);
	List<UIObs> getPatientObsData(String patientId);
}
