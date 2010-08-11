package org.openmrs.module.flowsheet.gwt.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents the Encounter as a UI model object
 * Required field of the Encounter class are included for mapping
 * 
 * @author umashanthi
 *
 */
public class UIEncounter implements IsSerializable {
	/* Attributes of the UIEncounter class */
	private Integer encounterId;
	private Date encounterDateTime;
	private String encounterType;
	private UIObs[] allObs;
	private String location;
	private String provider;
	private String creator;
	private Date dateCreated;
	/* 
	 * Constructor
	 */
	public UIEncounter() {

	}
	/* Getters and Setters */
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Date getEncounterDateTime() {
		return encounterDateTime;
	}

	public void setEncounterDateTime(Date encounterDateTime) {
		this.encounterDateTime = encounterDateTime;
	}

	public String getEncounterType() {
		return encounterType;
	}

	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}

	public UIObs[] getAllObs() {
		return allObs;
	}

	public void setAllObs(UIObs[] allObs) {
		this.allObs = allObs;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreator() {
		return creator;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateCreated() {
		return dateCreated;
	}

}
