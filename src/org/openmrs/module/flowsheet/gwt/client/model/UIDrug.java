package org.openmrs.module.flowsheet.gwt.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents the Drug class as a UI object model
 * 
 * @author umashanthi
 *
 */

public class UIDrug implements IsSerializable{
	/* Attributes */
	private Integer drugId;
	private double storageStrength;
	private String fullname;
	private double maximumDailyDosage;
	private double minimumDatilyDosage;
	private String unit;
	private boolean isCombination;
	
	/* Public no-arg constructor */
	public UIDrug(){
		
	}
	
	/* Getters and Setters */
	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public double getStorageStrength() {
		return storageStrength;
	}

	public void setStorageStrength(double storageStrength) {
		this.storageStrength = storageStrength;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public double getMaximumDailyDosage() {
		return maximumDailyDosage;
	}

	public void setMaximumDailyDosage(double maximumDailyDosage) {
		this.maximumDailyDosage = maximumDailyDosage;
	}

	public double getMinimumDatilyDosage() {
		return minimumDatilyDosage;
	}

	public void setMinimumDatilyDosage(double minimumDatilyDosage) {
		this.minimumDatilyDosage = minimumDatilyDosage;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isCombination() {
		return isCombination;
	}

	public void setCombination(boolean isCombination) {
		this.isCombination = isCombination;
	}

}
