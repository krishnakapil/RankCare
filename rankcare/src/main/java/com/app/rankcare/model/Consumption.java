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
	@Column(name="body_wt_mean")
	private String bodyWtMean;
	@Column(name="body_wt_sd")
	private String bodyWtSd;
	@Column(name="ci_data_1")
	private String ciData1;
	@Column(name="soil_inv_avg")
	private String soilInvAvg;
	@Column(name="soil_inv_gom_mean")
	private String soilInvGomMean;
	@Column(name="soil_inv_gom_sd")
	private String soilInvGomSd;
	@Column(name="water_cons_avg")
	private String waterConsAvg;
	@Column(name="ci_data_2")
	private String ciData2;
	@Column(name="water_inv_gom_mean")
	private String waterInvGomMean;
	@Column(name="water_inv_gom_sd")
	private String waterInvGomSd;

	public Consumption() {

	}

//	public Consumption(Long id, String ageGrp, String bodyWtMean, String ciData1, String soilInvAvg, String waterConsAvg,
//					   String ciData2) {
//		super();
//		this.id = id;
//		this.ageGrp = ageGrp;
//		this.bodyWtMean = bodyWtMean;
//		this.ciData1 = ciData1;
//		this.soilInvAvg = soilInvAvg;
//		this.waterConsAvg = waterConsAvg;
//		this.ciData2 = ciData2;
//	}
//
//
//	public Consumption(String ageGrp, String bodyWtMean, String ciData1, String soilInvAvg, String waterConsAvg,
//					   String ciData2) {
//		super();
//		this.ageGrp = ageGrp;
//		this.bodyWtMean = bodyWtMean;
//		this.ciData1 = ciData1;
//		this.soilInvAvg = soilInvAvg;
//		this.waterConsAvg = waterConsAvg;
//		this.ciData2 = ciData2;
//	}



	public Consumption(Long id, String ageGrp, String bodyWtMean, String bodyWtSd, String ciData1, String soilInvAvg,
					   String soilInvGomMean, String soilInvGomSd, String waterConsAvg, String ciData2, String waterInvGomMean,
					   String waterInvGomSd) {
		super();
		this.id = id;
		this.ageGrp = ageGrp;
		this.bodyWtMean = bodyWtMean;
		this.bodyWtSd = bodyWtSd;
		this.ciData1 = ciData1;
		this.soilInvAvg = soilInvAvg;
		this.soilInvGomMean = soilInvGomMean;
		this.soilInvGomSd = soilInvGomSd;
		this.waterConsAvg = waterConsAvg;
		this.ciData2 = ciData2;
		this.waterInvGomMean = waterInvGomMean;
		this.waterInvGomSd = waterInvGomSd;
	}

	public Consumption(String ageGrp, String bodyWtMean, String bodyWtSd, String ciData1, String soilInvAvg,
					   String soilInvGomMean, String soilInvGomSd, String waterConsAvg, String ciData2, String waterInvGomMean,
					   String waterInvGomSd) {
		super();
		this.ageGrp = ageGrp;
		this.bodyWtMean = bodyWtMean;
		this.bodyWtSd = bodyWtSd;
		this.ciData1 = ciData1;
		this.soilInvAvg = soilInvAvg;
		this.soilInvGomMean = soilInvGomMean;
		this.soilInvGomSd = soilInvGomSd;
		this.waterConsAvg = waterConsAvg;
		this.ciData2 = ciData2;
		this.waterInvGomMean = waterInvGomMean;
		this.waterInvGomSd = waterInvGomSd;
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

	public String getBodyWtMean() {
		return bodyWtMean;
	}

	public void setBodyWtMean(String bodyWtMean) {
		this.bodyWtMean = bodyWtMean;
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


	public String getBodyWtSd() {
		return bodyWtSd;
	}

	public void setBodyWtSd(String bodyWtSd) {
		this.bodyWtSd = bodyWtSd;
	}

	public String getSoilInvGomMean() {
		return soilInvGomMean;
	}

	public void setSoilInvGomMean(String soilInvGomMean) {
		this.soilInvGomMean = soilInvGomMean;
	}

	public String getSoilInvGomSd() {
		return soilInvGomSd;
	}

	public void setSoilInvGomSd(String soilInvGomSd) {
		this.soilInvGomSd = soilInvGomSd;
	}

	public String getWaterInvGomMean() {
		return waterInvGomMean;
	}

	public void setWaterInvGomMean(String waterInvGomMean) {
		this.waterInvGomMean = waterInvGomMean;
	}

	public String getWaterInvGomSd() {
		return waterInvGomSd;
	}

	public void setWaterInvGomSd(String waterInvGomSd) {
		this.waterInvGomSd = waterInvGomSd;
	}

	@Override
	public String toString() {
		return "Consumption [id=" + id + ", ageGrp=" + ageGrp + ", bodyWtMean=" + bodyWtMean + ", ciData1=" + ciData1
				+ ", soilInvAvg=" + soilInvAvg + ", waterConsAvg=" + waterConsAvg + ", ciData2=" + ciData2 + "]";
	}
}
