package org.openmrs.module.flowsheet.extension.html;

import java.util.Map;

import org.openmrs.module.Extension;

public class EncounterTabHeaderExt extends Extension {

	private String patientId = "";

	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	@Override
	public void initialize(Map<String, String> parameters) {
		patientId = parameters.get("patientId");
		System.out.println("patientId: " + patientId);
		System.out.println("parameters: " + parameters.keySet());
	}
	
	@Override
	public String getOverrideContent(String bodyContent) {
		return " &nbsp; <a href='module/flowsheet/flowsheet.list?patientId=" + patientId + "'>View Patient Flowsheet</a><br/> <br/>";
	}
	
}
