package com.app.rankcare.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.Site;
import com.app.rankcare.payload.SiteRegisterRequest;
import com.app.rankcare.repository.SiteDataRepository;

@RestController
@RequestMapping("/api/site")
public class SiteDataController {

    @Autowired
    private SiteDataRepository siteDataRepository;

    private static final Logger logger = LoggerFactory.getLogger(SiteDataController.class);

    @PostMapping("/register")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public String registerSite(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) {		
				 
    	Site site = new Site(siteRegisterRequest.getSiteId(),
    			siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
		  siteRegisterRequest.getState(), siteRegisterRequest.getOrgName());
		  
    	Site result = siteDataRepository.save(site);
    	logger.info("Saved Data Result::"+result.toString());
    	return "Site registered successfully";
    }
    
}