package com.app.rankcare.payload;

import javax.validation.constraints.*;

public class SiteRegisterRequest {
	  
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
 
}