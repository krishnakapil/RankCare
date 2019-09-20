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
	private String siteId; 	
	
	 
	@Column(name="chemical_name")
	private String chemicalName;

	 
	@Column(name="contamination_type")
	private String contaminationType;


	@Column(name="contamination_value")
	private String contaminationValue;
	
	public SiteCalculation() {
		
	}

	public SiteCalculation(String siteId, String chemicalName, String contaminationType,
			String contaminationValue) {
		super();
		this.siteId = siteId;
		this.chemicalName = chemicalName;
		this.contaminationType = contaminationType;
		this.contaminationValue = contaminationValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	
}
