package com.app.rankcare.payload;

import java.util.Map;

public class ConsumptionRequest {

    private Long id;
	
    private String ageGrp; 
    
    private String bodyWtAvg; 
    
    private String ciData1; 
    
    private String soilInvAvg; 
    
    private String waterConsAvg; 
    
    private String ciData2; 
	
	private Integer pageNo=0;
	
	private Integer pageSize=10;
	
	private String sortBy="id";
	
	private String orderBy="ASC";
	
	private Map<String,String> searchBy;

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

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Map<String, String> getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(Map<String, String> searchBy) {
		this.searchBy = searchBy;
	}
}
