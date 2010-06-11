package org.openmrs.module.flowsheet.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../../../moduleServlet/flowsheet/flowsheetService")
public interface FlowsheetService extends RemoteService{
	PatientObsCollection[] getObsData(String patientId);
}
