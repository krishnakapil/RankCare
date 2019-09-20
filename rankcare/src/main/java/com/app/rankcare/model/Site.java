package com.app.rankcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site_data")
public class Site extends DateAudit{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280440263317772534L;

	@Id
	@Column(name="site_id")
	private String siteId;

	@Column(name="site_name")
	private String siteName; 	
	

	@Column(name="site_location")
	private String siteLocation; 	
	 
	@Column(name="site_state")
	private String siteState;

	 
	@Column(name="site_org")
	private String siteOrg;

	public Site() {
		
	}

	public Site(String siteId, String siteName, String siteLocation, String state, String orgName) {
		this.siteId=siteId;
		this.siteName=siteName;
		this.siteLocation=siteLocation;
		this.siteState=state;
		this.siteOrg=orgName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
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

	public String getSiteState() {
		return siteState;
	}

	public void setSiteState(String siteState) {
		this.siteState = siteState;
	}

	public String getSiteOrg() {
		return siteOrg;
	}

	public void setSiteOrg(String siteOrg) {
		this.siteOrg = siteOrg;
	}

	
	@Override
	public String toString() {
		return "Site [siteId=" + siteId + ", siteName=" + siteName + ", siteLocation=" + siteLocation + ", siteState="
				+ siteState + ", siteOrg=" + siteOrg + "]";
	}
	
	
	
}
