package org.openmrs.module.flowsheet.gwt.client.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents the Concept model as a UI object model Necessary
 * attributes of the Concept class are included
 * 
 * @author umashanthi
 * 
 */
public class UIConcept implements IsSerializable {
	/* Attributes */
	private String conceptId;
	private String displayName;
	private List<String> answers;
	private String bestName;
	private String bestShortName;
	private String conceptClass;
	private String description;
	private List<String> names;
	private boolean isComplex;
	private boolean isNumeric;
	private boolean isSet;
	private boolean isNamed;
	private String dataType;
	private String units;
	private Double hiAbsoulute;
	private Double hiCritical;
	private Double hiNormal;
	private Double lowAbsolute;
	private Double lowNormal;
	private Double lowCritical;

	/* Public no-arg constructor */
	public UIConcept() {

	}
	
	/* Getters and Setters */

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Double getHiAbsoulute() {
		return hiAbsoulute;
	}

	public void setHiAbsoulute(Double hiAbsoulute) {
		this.hiAbsoulute = hiAbsoulute;
	}

	public Double getHiCritical() {
		return hiCritical;
	}

	public void setHiCritical(Double hiCritical) {
		this.hiCritical = hiCritical;
	}

	public Double getHiNormal() {
		return hiNormal;
	}

	public void setHiNormal(Double hiNormal) {
		this.hiNormal = hiNormal;
	}

	public Double getLowAbsolute() {
		return lowAbsolute;
	}

	public void setLowAbsolute(Double lowAbsolute) {
		this.lowAbsolute = lowAbsolute;
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

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String getBestName() {
		return bestName;
	}

	public void setBestName(String bestName) {
		this.bestName = bestName;
	}

	public String getBestShortName() {
		return bestShortName;
	}

	public void setBestShortName(String bestShortName) {
		this.bestShortName = bestShortName;
	}

	public String getConceptClass() {
		return conceptClass;
	}

	public void setConceptClass(String conceptClass) {
		this.conceptClass = conceptClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public boolean isComplex() {
		return isComplex;
	}

	public void setComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}

	public boolean isNumeric() {
		return isNumeric;
	}

	public void setNumeric(boolean isNumeric) {
		this.isNumeric = isNumeric;
	}

	public boolean isSet() {
		return isSet;
	}

	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}

	public boolean isNamed() {
		return isNamed;
	}

	public void setNamed(boolean isNamed) {
		this.isNamed = isNamed;
	}

}
