package com.app.rankcare.payload;

import com.app.rankcare.model.Site;
import com.app.rankcare.model.SiteCalculation;

import java.util.List;

public class SiteResponse {
    private Long id;

    private Long projectId;

    private String siteName;

    private String siteLocation;

    private String siteOrg;

    private Double siteLat;

    private Double siteLng;

    private List<SiteCalculation> siteCalculations;

    public SiteResponse(Site site, List<SiteCalculation> siteCalculations) {
        id = site.getId();
        projectId = site.getProjectId();
        siteName = site.getSiteName();
        siteLocation = site.getSiteLocation();
        siteOrg = site.getSiteOrg();
        siteLat = site.getSiteLat();
        siteLng = site.getSiteLng();
        this.siteCalculations = siteCalculations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public String getSiteOrg() {
        return siteOrg;
    }

    public void setSiteOrg(String siteOrg) {
        this.siteOrg = siteOrg;
    }

    public List<SiteCalculation> getSiteCalculations() {
        return siteCalculations;
    }

    public void setSiteCalculations(List<SiteCalculation> siteCalculations) {
        this.siteCalculations = siteCalculations;
    }

    public Double getSiteLat() {
        return siteLat;
    }

    public void setSiteLat(Double siteLat) {
        this.siteLat = siteLat;
    }

    public Double getSiteLng() {
        return siteLng;
    }

    public void setSiteLng(Double siteLng) {
        this.siteLng = siteLng;
    }

    @Override
    public String toString() {
        return "SiteResponse{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", siteName='" + siteName + '\'' +
                ", siteLocation='" + siteLocation + '\'' +
                ", siteOrg='" + siteOrg + '\'' +
                ", siteLat=" + siteLat +
                ", siteLng=" + siteLng +
                ", siteCalculations=" + siteCalculations +
                '}';
    }
}
