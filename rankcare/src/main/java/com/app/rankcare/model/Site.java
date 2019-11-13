package com.app.rankcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site_data")
public class Site extends DateAudit {

    private static final long serialVersionUID = 7280440263317772534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "site_location")
    private String siteLocation;

    @Column(name = "site_org")
    private String siteOrg;

    @Column(name = "site_lat")
    private Double siteLat;

    @Column(name = "site_lng")
    private Double siteLng;

    public Site() {

    }

    public Site(Long id, Long projectId, String siteName, String siteLocation, String orgName, Double siteLat, Double siteLng) {
        this.id = id;
        this.projectId = projectId;
        this.siteName = siteName;
        this.siteLocation = siteLocation;
        this.siteOrg = orgName;
        this.siteLat = siteLat;
        this.siteLng = siteLng;
    }

    public Site(Long projectId, String siteName, String siteLocation, String orgName, Double siteLat, Double siteLng) {
        this.projectId = projectId;
        this.siteName = siteName;
        this.siteLocation = siteLocation;
        this.siteOrg = orgName;
        this.siteLat = siteLat;
        this.siteLng = siteLng;
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
        return "Site{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", siteName='" + siteName + '\'' +
                ", siteLocation='" + siteLocation + '\'' +
                ", siteOrg='" + siteOrg + '\'' +
                ", siteLat=" + siteLat +
                ", siteLng=" + siteLng +
                '}';
    }
}
