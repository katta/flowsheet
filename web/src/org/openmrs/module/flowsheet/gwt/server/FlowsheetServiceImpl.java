package org.openmrs.module.flowsheet.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.gwt.client.FlowsheetService;
import org.openmrs.module.flowsheet.gwt.client.PatientObs;
import org.openmrs.module.flowsheet.gwt.client.PatientObsCollection;
import org.openmrs.module.flowsheet.gwt.client.model.UIConcept;
import org.openmrs.module.flowsheet.gwt.client.model.UIObs;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlowsheetServiceImpl extends RemoteServiceServlet implements
		FlowsheetService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date[] getDateRange(String patientId) {
		Date[] range = new Date[2];
		ObsService service = Context.getObsService();
		List<Person> patientIdList = new ArrayList<Person>();
		Patient patient = new Patient(Integer.valueOf(patientId));
		patientIdList.add(patient);

		// retrieve all observations for this person
		List<Obs> obsList = service.getObservations(patientIdList, null, null,
				null, null, null, null, null, null, null, null, false);
		range[0] = obsList.get(0).getObsDatetime(); // endDate
		int length = obsList.size();
		range[1] = new Date();
		while (length > 0) {
			if (obsList.get(length - 1) != null
					&& obsList.get(length - 1).getObsDatetime() != null) {
				range[1] = obsList.get(length - 1).getObsDatetime();
				break;

			} else {
				length--;
			}
		}

		return range;
	}

	// java.util.List<Obs> getObservations(java.util.List<Person> whom,
	// java.util.List<Encounter> encounters,
	// java.util.List<Concept> questions,
	// java.util.List<Concept> answers,
	// java.util.List<OpenmrsConstants.PERSON_TYPE> personTypes,
	// java.util.List<Location> locations,
	// java.util.List<java.lang.String> sort,
	// java.lang.Integer mostRecentN,
	// java.lang.Integer obsGroupId,
	// java.util.Date fromDate,
	// java.util.Date toDate,
	// boolean includeVoidedObs)
	public List<UIObs> getPatientObsData(String patientId, Date startDate,
			Date endDate) {
		List<UIObs> result = new ArrayList<UIObs>();
		ObsService service = Context.getObsService();
		List<Person> patientIdList = new ArrayList<Person>();
		Patient patient = new Patient(Integer.valueOf(patientId));
		patientIdList.add(patient);

		// retrieve all observations for this person
		List<Obs> obsList = service.getObservations(patientIdList, null, null,
				null, null, null, null, null, null, startDate, endDate, false);
		for (Obs obs : obsList) {
			UIObs uiObs = new UIObs();
			if (obs.getId() != null) {
				uiObs.setObsId(obs.getId());
			}
			if (obs.getObsDatetime() != null) {
				uiObs.setObsDateTime(obs.getObsDatetime());
			}
			if (obs.getLocation() != null) {
				if (obs.getLocation().getId() != null) {
					uiObs.setLocationId(obs.getLocation().getId().toString());
				}
				if (obs.getLocation().getName() != null) {
					uiObs.setLocation(obs.getLocation().getName());
				}
			}
			if (obs.getComment() != null) {
				uiObs.setComment(obs.getComment());
			}
			if (patient.getDateCreated() != null) {
				uiObs.setStartedDate(patient.getDateCreated());
			}
			if (patient.getDateVoided() != null) {
				uiObs.setEndedDate(patient.getDateVoided());
			}
			Concept concept;
			if ((concept = obs.getConcept()) != null) {
				UIConcept uiConcept = new UIConcept();
				if (concept.getId() != null) {
					uiConcept.setConceptId(concept.getId().toString());
				}
				if (concept.getDisplayString() != null) {
					uiConcept.setDisplayName(concept.getDisplayString());
				}

				Collection<ConceptAnswer> answers = concept.getAnswers();
				List<String> conceptAns = new ArrayList<String>();
				if (answers != null) {
					for (ConceptAnswer ans : answers) {
						if (ans != null
								&& ans.getAnswerConcept() != null
								&& ans.getAnswerConcept().getName() != null
								&& ans.getAnswerConcept().getName().getName() != null) {
							conceptAns.add(ans.getAnswerConcept().getName()
									.getName());
						}
					}
				}
				uiConcept.setAnswers(conceptAns);
				if (concept.getConceptClass() != null
						&& concept.getConceptClass().getName() != null) {
					uiConcept.setConceptClass(concept.getConceptClass()
							.getName());
				}
				if (concept.getDescription() != null
						&& concept.getDescription().getDescription() != null) {
					uiConcept.setDescription(concept.getDescription()
							.getDescription());
				}
				uiConcept.setComplex(concept.isComplex());
				uiConcept.setNumeric(concept.isNumeric());
				uiConcept.setSet(concept.isSet());
				if (concept.getDatatype() != null
						&& concept.getDatatype().getName() != null) {
					uiConcept.setDataType(concept.getDatatype().getName());
				}
				if (concept.getDatatype().isNumeric()) {
					ConceptNumeric conceptNumeric = Context.getConceptService()
							.getConceptNumeric(concept.getConceptId());
					if (conceptNumeric != null) {
						if (conceptNumeric.getUnits() != null) {
							uiConcept.setUnits(conceptNumeric.getUnits());
						}
						if (conceptNumeric.getHiAbsolute() != null) {
							uiConcept.setHiAbsoulute(conceptNumeric
									.getHiAbsolute());
						}
						if (conceptNumeric.getHiNormal() != null) {
							uiConcept.setHiNormal(conceptNumeric.getHiNormal());
						}
						if (conceptNumeric.getHiCritical() != null) {
							uiConcept.setHiCritical(conceptNumeric
									.getHiCritical());
						}
						if (conceptNumeric.getLowAbsolute() != null) {
							uiConcept.setLowAbsolute(conceptNumeric
									.getLowAbsolute());
						}
						if (conceptNumeric.getLowNormal() != null) {
							uiConcept.setLowNormal(conceptNumeric
									.getLowNormal());
						}
						if (conceptNumeric.getLowCritical() != null) {
							uiConcept.setLowCritical(conceptNumeric
									.getLowCritical());
						}

					}
				}

				uiObs.setConcepts(uiConcept);
			}
			if (obs.getValueBoolean() != null) {
				uiObs.setBooleanValue(obs.getValueBoolean());
			}
			if (obs.getValueNumeric() != null) {
				uiObs.setNumericValue(obs.getValueNumeric());
			}
			if (obs.getValueText() != null) {
				uiObs.setTextValue(obs.getValueText());
			}
			// if(obs.getValueCoded()!=null &&
			// obs.getValueCoded().getName().getName()!=null){
			// uiObs.setCodedValue(obs.getValueCoded().getName().getName());
			// }
			result.add(uiObs);
		}
		return result;
	}

	public Map<Integer, String> getConceptList() {
		Map<Integer, String> conceptMap = new HashMap<Integer, String>();
		ConceptService conceptService = Context.getConceptService();
		List<Concept> conceptList = conceptService.getAllConcepts();
		for (Concept concept : conceptList) {
			if(concept!=null && concept.getId()!=null && concept.getDisplayString()!=null)
			conceptMap.put(concept.getId(), concept.getDisplayString());
		}
		return conceptMap;
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
