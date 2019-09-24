package com.app.rankcare.payload;

public class ToxicityRequest {

    private Long id;
	
	private String chemicalName;
	
	private String chemicalFormula;
	
	private String soilGuideline;
	
	private String soilRef;

	private String waterGuideline;
	
	private String waterRef;

	private String dosageRef;
	
	private String reference;

	private String cancerSlopeFactor;
	
	private String cancerSlopeRef;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public String getChemicalFormula() {
		return chemicalFormula;
	}

	public void setChemicalFormula(String chemicalFormula) {
		this.chemicalFormula = chemicalFormula;
	}

	public String getSoilGuideline() {
		return soilGuideline;
	}

	public void setSoilGuideline(String soilGuideline) {
		this.soilGuideline = soilGuideline;
	}

	public String getSoilRef() {
		return soilRef;
	}

	public void setSoilRef(String soilRef) {
		this.soilRef = soilRef;
	}

	public String getWaterGuideline() {
		return waterGuideline;
	}

	public void setWaterGuideline(String waterGuideline) {
		this.waterGuideline = waterGuideline;
	}

	public String getWaterRef() {
		return waterRef;
	}

	public void setWaterRef(String waterRef) {
		this.waterRef = waterRef;
	}

	public String getDosageRef() {
		return dosageRef;
	}

	public void setDosageRef(String dosageRef) {
		this.dosageRef = dosageRef;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCancerSlopeFactor() {
		return cancerSlopeFactor;
	}

	public void setCancerSlopeFactor(String cancerSlopeFactor) {
		this.cancerSlopeFactor = cancerSlopeFactor;
	}

	public String getCancerSlopeRef() {
		return cancerSlopeRef;
	}

	public void setCancerSlopeRef(String cancerSlopeRef) {
		this.cancerSlopeRef = cancerSlopeRef;
	}
	
	
}
