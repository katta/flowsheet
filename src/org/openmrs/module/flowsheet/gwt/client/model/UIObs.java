package org.openmrs.module.flowsheet.gwt.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents the Obs class as a UI object model
 * Necessary attributes of the Obs class are included for the purpose of mappting
 * 
 * @author umashanthi
 *
 */

public class UIObs implements IsSerializable{
	/* Attributes of the UIObs class */
	private Integer obsId;
	private Date obsDateTime;
	private String locationId;
	private String comment;
	private String conceptDescription;
	private UIConcept concept;
	private UIEncounter encounter;
	private Date startedDate;
	private Date endedDate;
	private String location;
	private UIObs obsGroup;
	private UIObs[] relatedObservations;
	private Boolean booleanValue;
	private String stringValue;
	private String codedValue;
	private Double numericValue;
	private String textValue;
	private UIDrug drugValue;
	private Integer creator;
	private Date createdDate;
	
	/* Public no-arg constructor */
	public UIObs(){
		
	}
	/* Getters and Setters */
	public Integer getObsId() {
		return obsId;
	}

	public void setObsId(Integer obsId) {
		this.obsId = obsId;
	}

	public Date getObsDateTime() {
		return obsDateTime;
	}

	public void setObsDateTime(Date obsDateTime) {
		this.obsDateTime = obsDateTime;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getConceptDescription() {
		return conceptDescription;
	}

	public void setConceptDescription(String conceptDescription) {
		this.conceptDescription = conceptDescription;
	}

	public UIConcept getConcepts() {
		return concept;
	}

	public void setConcepts(UIConcept concept) {
		this.concept = concept;
	}

	public UIEncounter getEncounter() {
		return encounter;
	}

	public void setEncounter(UIEncounter encounter) {
		this.encounter = encounter;
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	public Date getEndedDate() {
		return endedDate;
	}

	public void setEndedDate(Date endedDate) {
		this.endedDate = endedDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UIObs getObsGroup() {
		return obsGroup;
	}

	public void setObsGroup(UIObs obsGroup) {
		this.obsGroup = obsGroup;
	}

	public UIObs[] getRelatedObservations() {
		return relatedObservations;
	}

	public void setRelatedObservations(UIObs[] relatedObservations) {
		this.relatedObservations = relatedObservations;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getCodedValue() {
		return codedValue;
	}

	public void setCodedValue(String codedValue) {
		this.codedValue = codedValue;
	}

	public Double getNumericValue() {
		return numericValue;
	}

	public void setNumericValue(Double numericValue) {
		this.numericValue = numericValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public UIDrug getDrugValue() {
		return drugValue;
	}

	public void setDrugValue(UIDrug drugValue) {
		this.drugValue = drugValue;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
