package org.openmrs.module.flowsheet.gwt.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UINumericData implements IsSerializable{
	private Date obsDate;
	private Double obsValue;
	private String conceptName;
	private Double minValue;
	private Double maxValue;
	private String unit;
	 public UINumericData(){
		 
	 }

	public void setObsDate(Date obsDate) {
		this.obsDate = obsDate;
	}

	public Date getObsDate() {
		return obsDate;
	}

	public void setObsValue(Double obsValue) {
		this.obsValue = obsValue;
	}

	public Double getObsValue() {
		return obsValue;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getConceptName() {
		return conceptName;
	}
}
