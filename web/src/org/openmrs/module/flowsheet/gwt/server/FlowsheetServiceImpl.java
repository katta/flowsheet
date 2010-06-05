package org.openmrs.module.flowsheet.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.gwt.client.FlowsheetService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlowsheetServiceImpl extends RemoteServiceServlet implements
		FlowsheetService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String[] getObsData(String patientId) {

		EncounterService encounterService = Context.getEncounterService();
		List<Encounter> encounters = encounterService
				.getEncountersByPatientId(Integer.valueOf(patientId));
		Collections.sort(encounters, new EncounterComparator());
		List<String> result = new ArrayList<String>();
		for (Encounter entry : encounters) {
			Set<Obs> observations = entry.getAllObs();
			for (Obs data : observations) {
				//if (data.getConcept().getDatatype()!=null && data.getConcept().getDatatype().getHl7Abbreviation() != null) {
					String detail = entry.getDateCreated().toString()
							+ "#"
							+ data.getConcept().getDisplayString()
							+ "#"
							+ data.getComment();
					result.add(detail);
				//}
			}
		}

		String[] res = new String[result.size()];
		res = result.toArray(res);
		return res;
	}

}

class EncounterComparator implements Comparator<Encounter> {

	public int compare(Encounter o1, Encounter o2) {
		if (o1.getDateCreated().getTime() > o2.getDateCreated().getTime()) {
			return -1;
		} else {
			return 1;
		}
	}

}
