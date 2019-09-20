package com.app.rankcare.payload;

public class SiteContaminantData {

	private String chemicalName;

	private String contaminationType;

	private String contaminationValue;
	
	private String activeYN; 

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public String getContaminationType() {
		return contaminationType;
	}

	public void setContaminationType(String contaminationType) {
		this.contaminationType = contaminationType;
	}

	public String getContaminationValue() {
		return contaminationValue;
	}

	public void setContaminationValue(String contaminationValue) {
		this.contaminationValue = contaminationValue;
	}

	public String getActiveYN() {
		return activeYN;
	}

	public void setActiveYN(String activeYN) {
		this.activeYN = activeYN;
	}
	
	
}
