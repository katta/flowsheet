package org.openmrs.module.flowsheet.gwt.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ObsData extends BaseModel {
	private static final long serialVersionUID = 1L;
	

	public ObsData() {
	}

	public ObsData(String date, String conceptName, String value, String drug) {
		set("date", date);
		set("conceptName", conceptName);
		set("value", value);
		set("drug", drug);
		}
		
		public String getDate() {
		return (String) get("date");
		}
		public String getConceptName() {
		return (String) get("conceptName");
		}
		public String getValue() {
		return (String) get("value");
		}
		public double getDrug() {
		Double salary = (Double) get("drug");
		return salary.doubleValue();
		}
		public String toString() {
		return getConceptName();
		}}