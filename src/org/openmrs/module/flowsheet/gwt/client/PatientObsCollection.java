package org.openmrs.module.flowsheet.gwt.client;


import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PatientObsCollection implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date encounterDateTime;
	private PatientObs[] drugs;
	private PatientObs[] tests;
	private PatientObs[] questions;
	private PatientObs[] misc;
public PatientObsCollection(){
	
}
	
	public void setEncounterDateTime(Date encounterDateTime) {
		this.encounterDateTime = encounterDateTime;
	}

	public Date getEncounterDateTime() {
		return encounterDateTime;
	}

	public void setDrugs(PatientObs[] drugs) {
		this.drugs = drugs;
	}

	public PatientObs[] getDrugs() {
		return drugs;
	}

	public void setTests(PatientObs[] tests) {
		this.tests = tests;
	}

	public PatientObs[] getTests() {
		return tests;
	}

	public void setQuestions(PatientObs[] questions) {
		this.questions = questions;
	}

	public PatientObs[] getQuestions() {
		return questions;
	}

	public void setMisc(PatientObs[] misc) {
		this.misc = misc;
	}

	public PatientObs[] getMisc() {
		return misc;
	}
}
