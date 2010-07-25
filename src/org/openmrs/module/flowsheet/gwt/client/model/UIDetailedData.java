package org.openmrs.module.flowsheet.gwt.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UIDetailedData implements IsSerializable{
	private Integer obsId;
	private Date obsDate;
	private Double obsValue;
	private String conceptName;
	private Double minValue;
	private Double maxValue;
	public Double getHiNormal() {
		return hiNormal;
	}


	public void setHiNormal(Double hiNormal) {
		this.hiNormal = hiNormal;
	}


	public Double getHiCritical() {
		return hiCritical;
	}


	public void setHiCritical(Double hiCritical) {
		this.hiCritical = hiCritical;
	}


	public Double getLowNormal() {
		return lowNormal;
	}


	public void setLowNormal(Double lowNormal) {
		this.lowNormal = lowNormal;
	}


	public Double getLowCritical() {
		return lowCritical;
	}


	public void setLowCritical(Double lowCritical) {
		this.lowCritical = lowCritical;
	}


	private Double hiNormal;
	private Double hiCritical;
	private Double lowNormal;
	private Double lowCritical;
	private String unit;
	private String stringValue;
	private boolean isNumeric;
	 public UIDetailedData(){
		 
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

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setNumeric(boolean isNumeric) {
		this.isNumeric = isNumeric;
	}

	public boolean isNumeric() {
		return isNumeric;
	}


	public void setObsId(Integer obsId) {
		this.obsId = obsId;
	}


	public Integer getObsId() {
		return obsId;
	}
}
