package org.openmrs.module.flowsheet.gwt.server;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.gwt.client.FlowsheetService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlowsheetServiceImpl extends RemoteServiceServlet implements FlowsheetService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getObsData(String patientId) {
		ObsService service=Context.getObsService();
		Obs obs=service.getObservationsByPerson(new Patient(Integer.valueOf(patientId))).get(1);
		String result=obs.getId()+"  "+obs.getDateCreated().toString()+"   "+obs.getConcept().getDisplayString()+"  "+obs.getComment();
		return result;
	}

}
