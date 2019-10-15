package com.app.rankcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site_calculation")
public class SiteCalculation extends DateAudit{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5252055900338280679L;

	/**
	 * 
	 */
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="site_id")
	private Long siteId; 	
	
	 
	@Column(name="chemical_id")
	private Long chemicalId;

	 
	@Column(name="contamination_type")
	private String contaminationType;


	@Column(name="contamination_value")
	private String contaminationValue;
	
	@Column(name="active_yn")
	private String activeYN;
	
	public SiteCalculation() {
		
	}

	public SiteCalculation(Long siteId, Long chemicalId, String contaminationType,
			String contaminationValue,String activeYN) {
		super();
		this.siteId = siteId;
		this.chemicalId = chemicalId;
		this.contaminationType = contaminationType;
		this.contaminationValue = contaminationValue;
		this.activeYN=activeYN;
	}
	public SiteCalculation(Long id,Long siteId, Long chemicalId, String contaminationType,
			String contaminationValue,String activeYN) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.chemicalId = chemicalId;
		this.contaminationType = contaminationType;
		this.contaminationValue = contaminationValue;
		this.activeYN=activeYN;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}


	public Long getChemicalId() {
		return chemicalId;
	}

	public void setChemicalId(Long chemicalId) {
		this.chemicalId = chemicalId;
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
