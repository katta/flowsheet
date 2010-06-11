package org.openmrs.module.flowsheet.gwt.client;


import com.google.gwt.user.client.rpc.IsSerializable;

public class PatientObs implements IsSerializable {

	private String conceptName;
	private String obsValue;
	private String[] drugs;
	public PatientObs(){
		
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setObsValue(String obsValue) {
		this.obsValue = obsValue;
	}

	public String getObsValue() {
		return obsValue;
	}

	public void setDrugs(String[] drugs) {
		this.drugs = drugs;
	}

	public String[] getDrugs() {
		return drugs;
	}

}
