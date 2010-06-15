package org.openmrs.module.flowsheet.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
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

	public PatientObsCollection[] getObsData(String patientId) {

		EncounterService encounterService = Context.getEncounterService();
		List<Encounter> encounters = encounterService
				.getEncountersByPatientId(Integer.valueOf(patientId));
		Collections.sort(encounters, new EncounterComparator());
		
		List<PatientObsCollection> result = new ArrayList<PatientObsCollection>();
		for (Encounter entry : encounters) {
			List<PatientObs> questions = new ArrayList<PatientObs>();
			List<PatientObs> tests = new ArrayList<PatientObs>();
			List<PatientObs> drugs = new ArrayList<PatientObs>();
			List<PatientObs> misc = new ArrayList<PatientObs>();
			PatientObsCollection collection = new PatientObsCollection();
			Set<Obs> observations = entry.getAllObs();
			for (Obs data : observations) {
				PatientObs patientData = new PatientObs();
				patientData.setConceptName(data.getConcept().getName()
						.getName());
				String value = " ";
				if (data.getConcept().getDatatype().isBoolean()) {
					value = data.getValueAsBoolean() ? "Yes" : "No";

				} else if (data.getConcept().getDatatype().isNumeric()) {
					value = data.getValueNumeric() != null ? data
							.getValueNumeric()
							+ "" : " ";
				} else if (data.getConcept().getDatatype().isText()) {
					value = data.getValueText() != null ? data.getValueText()
							: " ";
				} else if (data.getConcept().getDatatype().isCoded()) {
					value = data.getValueModifier() != null ? data
							.getValueModifier() : " ";
				}
				patientData.setObsValue(value);
				if (data.getConcept().getConceptClass() != null
						&& data.getConcept().getConceptClass().getName() != null) {
					String conceptClass = data.getConcept().getConceptClass()
							.getName();
					if (conceptClass.equalsIgnoreCase("Question")) {
						questions.add(patientData);
					} else if (conceptClass.equalsIgnoreCase("Test")) {
						tests.add(patientData);
					} else {
						List<String> drug = new ArrayList<String>();
						if (data.getConcept().getAnswers() != null) {
							Collection<ConceptAnswer> answers = data
									.getConcept().getAnswers();
							Iterator<ConceptAnswer> iterator = answers
									.iterator();
							while (iterator.hasNext()) {
								ConceptAnswer ans = iterator.next();
								if (ans.getAnswerDrug() != null
										&& ans.getAnswerDrug().getName() != null) {
									drug.add(ans.getAnswerDrug().getName());
								}
							}
							String[] drugArray = new String[drug.size()];
							patientData.setDrugs(drug.toArray(drugArray));
							drugs.add(patientData);
						} else {
							misc.add(patientData);
						}

					}
				}
				// if (data.getConcept().getDatatype()!=null &&
				// data.getConcept().getDatatype().getHl7Abbreviation() != null)
				// {
				/*
				 * String value=" ";
				 * 
				 * 
				 * if(data.getConcept().getDatatype().isBoolean()){
				 * value=data.getValueAsBoolean()?"Yes":"No";
				 * 
				 * } else if(data.getConcept().getDatatype().isNumeric()){
				 * value=
				 * data.getValueNumeric()!=null?data.getValueNumeric()+"":" "; }
				 * else if(data.getConcept().getDatatype().isText()){
				 * value=data.getValueText()!=null?data.getValueText():" "; }
				 * else if(data.getConcept().getDatatype().isCoded()){
				 * value=data
				 * .getValueModifier()!=null?data.getValueModifier():" "; }
				 */
				/*
				 * String conceptClass=" ";
				 * if(data.getConcept().getConceptClass()!=null &&
				 * data.getConcept().getConceptClass().getName()!=null){
				 * conceptClass=data.getConcept().getConceptClass().getName(); }
				 * if(data.getConcept().getAnswers()!=null){
				 * Collection<ConceptAnswer>
				 * answers=data.getConcept().getAnswers();
				 * Iterator<ConceptAnswer> iterator=answers.iterator();
				 * while(iterator.hasNext()){ ConceptAnswer ans=iterator.next();
				 * if(ans.getAnswerDrug()!=null &&
				 * ans.getAnswerDrug().getName()!=null){
				 * conceptClass+=ans.getAnswerDrug().getName(); } } } String
				 * conceptName=" ";
				 * conceptName=data.getConcept().getName().getName();
				 * 
				 * String detail = entry.getDateCreated().toString() + "#" +
				 * conceptName + "#" + value +"#" +conceptClass;
				 * 
				 * result.add(detail); //}
				 */

			}
			PatientObs[] drugsArray = new PatientObs[drugs.size()];
			PatientObs[] questionsArray = new PatientObs[questions.size()];
			PatientObs[] testsArray = new PatientObs[tests.size()];
			PatientObs[] miscArray = new PatientObs[misc.size()];
			collection.setDrugs(drugs.toArray(drugsArray));
			collection.setTests(tests.toArray(testsArray));
			collection.setQuestions(questions.toArray(questionsArray));
			collection.setMisc(misc.toArray(miscArray));
			collection.setEncounterDateTime(entry.getDateCreated());
			result.add(collection);
			}
			PatientObsCollection[] resultArray=new PatientObsCollection[result.size()];
		/*
		 * String[] res = new String[result.size()]; res = result.toArray(res);
		 * return res;
		 */
		return result.toArray(resultArray);
	}
	
//	java.util.List<Obs> getObservations(java.util.List<Person> whom,
//            java.util.List<Encounter> encounters,
//            java.util.List<Concept> questions,
//            java.util.List<Concept> answers,
//            java.util.List<OpenmrsConstants.PERSON_TYPE> personTypes,
//            java.util.List<Location> locations,
//            java.util.List<java.lang.String> sort,
//            java.lang.Integer mostRecentN,
//            java.lang.Integer obsGroupId,
//            java.util.Date fromDate,
//            java.util.Date toDate,
//            boolean includeVoidedObs)
	public List<UIObs> getPatientObsData(String patientId){
		List<UIObs> result=new ArrayList<UIObs>();
		ObsService service=Context.getObsService();
		List<Person> patientIdList=new ArrayList<Person>();
		patientIdList.add(new Patient(Integer.valueOf(patientId)));
		// retrieve all observations for this person
		List<Obs> obsList=service.getObservations(patientIdList,null,null,null,null,null,null,null,null,null,null,false);
		for(Obs obs:obsList){
			UIObs uiObs=new UIObs();
			if(obs.getId()!=null){
				uiObs.setObsId(obs.getId());
			}
			if(obs.getObsDatetime()!=null){
				uiObs.setObsDateTime(obs.getObsDatetime());
			}
			if(obs.getLocation()!=null){
				if(obs.getLocation().getId()!=null){
					uiObs.setLocationId(obs.getLocation().getId().toString());
				}
				if(obs.getLocation().getName()!=null){
					uiObs.setLocation(obs.getLocation().getName());
				}
			}
			if(obs.getComment()!=null){
				uiObs.setComment(obs.getComment());
			}
			Concept concept;
			if((concept=obs.getConcept())!=null){
				UIConcept uiConcept=new UIConcept();
				if(concept.getId()!=null){
					uiConcept.setConceptId(concept.getId().toString());
				}
				if(concept.getDisplayString()!=null){
					uiConcept.setDisplayName(concept.getDisplayString());
				}
				
				Collection<ConceptAnswer> answers=concept.getAnswers();
				List<String> conceptAns=new ArrayList<String>();
				if(answers!=null){
					for(ConceptAnswer ans:answers){
						if(ans!=null && ans.getAnswerConcept()!=null && ans.getAnswerConcept().getName()!=null && ans.getAnswerConcept().getName().getName()!=null){
							conceptAns.add(ans.getAnswerConcept().getName().getName());
						}
					}
				}
				uiConcept.setAnswers(conceptAns);
				if(concept.getConceptClass()!=null && concept.getConceptClass().getName()!=null){
					uiConcept.setConceptClass(concept.getConceptClass().getName());
				}
				if(concept.getDescription()!=null && concept.getDescription().getDescription()!=null){
					uiConcept.setDescription(concept.getDescription().getDescription());
				}
				uiConcept.setComplex(concept.isComplex());
				uiConcept.setNumeric(concept.isNumeric());
				uiConcept.setSet(concept.isSet());
				if(concept.getDatatype()!=null && concept.getDatatype().getName()!=null){
					uiConcept.setDataType(concept.getDatatype().getName());
				}
				if(concept.getDatatype().isNumeric()){
					ConceptNumeric conceptNumeric=Context.getConceptService().getConceptNumeric(concept.getConceptId());
					if(conceptNumeric!=null){
						if(conceptNumeric.getUnits()!=null){                      
							uiConcept.setUnits(conceptNumeric.getUnits());
						}
						if(conceptNumeric.getHiAbsolute()!=null){
							uiConcept.setHiAbsoulute(conceptNumeric.getHiAbsolute());
						}
						if(conceptNumeric.getHiNormal()!=null){
							uiConcept.setHiNormal(conceptNumeric.getHiNormal());
						}
						if(conceptNumeric.getHiCritical()!=null){
							uiConcept.setHiCritical(conceptNumeric.getHiCritical());
						}
						if(conceptNumeric.getLowAbsolute()!=null){
							uiConcept.setLowAbsolute(conceptNumeric.getLowAbsolute());
						}
						if(conceptNumeric.getLowNormal()!=null){
							uiConcept.setLowNormal(conceptNumeric.getLowNormal());
						}
						if(conceptNumeric.getLowCritical()!=null){
							uiConcept.setLowCritical(conceptNumeric.getLowCritical());
						}
						
					}
				}
				
				uiObs.setConcepts(uiConcept);
			}
			if(obs.getValueBoolean()!=null){
				uiObs.setBooleanValue(obs.getValueBoolean());
			}
			if(obs.getValueNumeric()!=null){
				uiObs.setNumericValue(obs.getValueNumeric());
			}
			if(obs.getValueText()!=null){
				uiObs.setTextValue(obs.getValueText());
			}
//			if(obs.getValueCoded()!=null && obs.getValueCoded().getName().getName()!=null){
//				uiObs.setCodedValue(obs.getValueCoded().getName().getName());
//			}
			result.add(uiObs);
		}
		return result;
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
