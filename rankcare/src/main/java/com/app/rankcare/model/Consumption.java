package com.app.rankcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="consumption_data")
public class Consumption extends DateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3578212100596393592L;


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name="age_grp")
	private String ageGrp;
	@Column(name="body_wt_avg")
	private String bodyWtAvg;
	@Column(name="ci_data_1")
	private String ciData1;
	@Column(name="soil_inv_avg")
	private String soilInvAvg;
	@Column(name="water_cons_avg")
	private String waterConsAvg;
	@Column(name="ci_data_2")
	private String ciData2;

	public Consumption() {
		
	}
	 
	public Consumption(Long id, String ageGrp, String bodyWtAvg, String ciData1, String soilInvAvg, String waterConsAvg,
			String ciData2) {
		super();
		this.id = id;
		this.ageGrp = ageGrp;
		this.bodyWtAvg = bodyWtAvg;
		this.ciData1 = ciData1;
		this.soilInvAvg = soilInvAvg;
		this.waterConsAvg = waterConsAvg;
		this.ciData2 = ciData2;
	}

	
	public Consumption(String ageGrp, String bodyWtAvg, String ciData1, String soilInvAvg, String waterConsAvg,
			String ciData2) {
		super();
		this.ageGrp = ageGrp;
		this.bodyWtAvg = bodyWtAvg;
		this.ciData1 = ciData1;
		this.soilInvAvg = soilInvAvg;
		this.waterConsAvg = waterConsAvg;
		this.ciData2 = ciData2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgeGrp() {
		return ageGrp;
	}

	public void setAgeGrp(String ageGrp) {
		this.ageGrp = ageGrp;
	}

	public String getBodyWtAvg() {
		return bodyWtAvg;
	}

	public void setBodyWtAvg(String bodyWtAvg) {
		this.bodyWtAvg = bodyWtAvg;
	}

	public String getCiData1() {
		return ciData1;
	}

	public void setCiData1(String ciData1) {
		this.ciData1 = ciData1;
	}

	public String getSoilInvAvg() {
		return soilInvAvg;
	}

	public void setSoilInvAvg(String soilInvAvg) {
		this.soilInvAvg = soilInvAvg;
	}

	public String getWaterConsAvg() {
		return waterConsAvg;
	}

	public void setWaterConsAvg(String waterConsAvg) {
		this.waterConsAvg = waterConsAvg;
	}

	public String getCiData2() {
		return ciData2;
	}

	public void setCiData2(String ciData2) {
		this.ciData2 = ciData2;
	}
 
	
		
}
