package com.app.rankcare.payload;

import java.util.Map;

public class ToxicityRequest {

    private Long id;

    private String chemicalName;

    private String chemicalFormula;

    private String soilGuideline;

    private String soilRef;

    private String waterGuideline;

    private String waterRef;

    private String cancerSlopeFactor;

    private String cancerSlopeRef;

    private String dosageRefValue;

    private String reference;

    private Integer pageNo = 0;

    private Integer pageSize = 10;

    private String sortBy = "id";

    private String orderBy = "ASC";

    private Map<String, String> searchBy;

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

    public String getDosageRefValue() {
        return dosageRefValue;
    }

    public String getReference() {
        return reference;
    }
}
