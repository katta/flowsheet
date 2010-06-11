package org.openmrs.module.flowsheet.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.flowsheet.gwt.client.FlowsheetService;
import org.openmrs.module.flowsheet.gwt.client.PatientObs;
import org.openmrs.module.flowsheet.gwt.client.PatientObsCollection;

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
