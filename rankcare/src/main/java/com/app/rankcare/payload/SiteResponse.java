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

    private List<SiteCalculation> siteCalculations;

    public SiteResponse(Site site, List<SiteCalculation> siteCalculations) {
        id = site.getId();
        projectId = site.getProjectId();
        siteName = site.getSiteName();
        siteLocation = site.getSiteLocation();
        siteOrg = site.getSiteOrg();
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
}
