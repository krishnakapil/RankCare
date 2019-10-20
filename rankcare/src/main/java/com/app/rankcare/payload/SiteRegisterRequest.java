package com.app.rankcare.payload;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SiteRegisterRequest {
    private Long id;

    @NotBlank
    @Size(min = 4, max = 40)
    private String siteName;

    @NotBlank
    @Size(min = 3, max = 40)
    private String siteLocation;

    @NotBlank
    @Size(max = 40)
    private String state;

    @Size(min = 2, max = 15)
    private String siteId;

    @NotBlank
    @Size(min = 3, max = 40)
    private String orgName;

    private List<SiteContaminantData> siteContaminant;

    private Map<String, Double> t1;

    private Map<String, Map<String, Double>> t2;

    private Map<String, Map<String, Double>> t3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<SiteContaminantData> getSiteContaminant() {
        return siteContaminant;
    }

    public void setSiteContaminant(List<SiteContaminantData> siteContaminant) {
        this.siteContaminant = siteContaminant;
    }

    public Map<String, Double> getT1() {
        return t1;
    }

    public void setT1(Map<String, Double> t1) {
        this.t1 = t1;
    }

    public Map<String, Map<String, Double>> getT2() {
        return t2;
    }

    public void setT2(Map<String, Map<String, Double>> t2) {
        this.t2 = t2;
    }

    public Map<String, Map<String, Double>> getT3() {
        return t3;
    }

    public void setT3(Map<String, Map<String, Double>> t3) {
        this.t3 = t3;
    }
}