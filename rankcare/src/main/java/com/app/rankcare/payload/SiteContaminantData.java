package com.app.rankcare.payload;

public class SiteContaminantData {

	private Long id;
	

	private Long chemicalId;
	
	private String chemicalName;

	private String contaminationType;

	private String contaminationValue;
	
	private String activeYN; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChemicalId() {
		return chemicalId;
	}

	public void setChemicalId(Long chemicalId) {
		this.chemicalId = chemicalId;
	}

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
