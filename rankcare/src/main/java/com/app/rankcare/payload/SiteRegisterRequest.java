package com.app.rankcare.payload;

import java.util.List;

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

    @NotBlank
    @Size(min = 2, max = 15)
    private String siteId;
    
    @NotBlank
    @Size(min = 3, max = 40)
    private String orgName;
    
    
    private List<SiteContaminantData> siteContaminant;

    private List<Long> siteIds;
    
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

	public List<Long> getSiteIds() {
		return siteIds;
	}

	public void setSiteIds(List<Long> siteIds) {
		this.siteIds = siteIds;
	}
 
}