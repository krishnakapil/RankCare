package com.app.rankcare.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.app.rankcare.model.SiteCalculation;
import com.app.rankcare.payload.SiteContaminantData;
import com.app.rankcare.payload.SiteRegisterRequest;
import com.app.rankcare.repository.SiteCalculationRepository;
import com.app.rankcare.repository.SiteDataRepository;

@RestController
@RequestMapping("/api/site")
public class SiteDataController {

    @Autowired
    private SiteDataRepository siteDataRepository;


    @Autowired
    private SiteCalculationRepository siteCalculationRepository;

    private static final Logger logger = LoggerFactory.getLogger(SiteDataController.class);

    @PostMapping("/register")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public String registerSite(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) {		
		
    	Site result = siteDataRepository.save(new Site(siteRegisterRequest.getSiteId(),
    			siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
		  siteRegisterRequest.getState(), siteRegisterRequest.getOrgName()));
    	if(siteRegisterRequest.getSiteContaminant()!=null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
    		SiteCalculation res;
    		for(SiteContaminantData contaminantData:siteRegisterRequest.getSiteContaminant()){
    			res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getSiteId(),contaminantData.getChemicalName(),contaminantData.getContaminationType(),contaminantData.getContaminationValue(),"Y"));
    			logger.info("Data Saved>"+res);
    		}
    	}
    	logger.info("Saved Data Result::"+result.toString());
    	return "Site registered successfully";
    }
    
    @PostMapping("/updateRegistration")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public String updateSiteRegistration(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) {		
		
    	Site result = siteDataRepository.save(new Site(siteRegisterRequest.getSiteId(),
    			siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
		  siteRegisterRequest.getState(), siteRegisterRequest.getOrgName()));
    	if(siteRegisterRequest.getSiteContaminant()!=null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
    		SiteCalculation res;
    		for(SiteContaminantData contaminantData:siteRegisterRequest.getSiteContaminant()){
    			if(contaminantData.getId()!=null) {
    				res = siteCalculationRepository.save(new SiteCalculation(contaminantData.getId(),siteRegisterRequest.getSiteId(),contaminantData.getChemicalName(),contaminantData.getContaminationType(),contaminantData.getContaminationValue(),contaminantData.getActiveYN()));
    			}else {
    				res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getSiteId(),contaminantData.getChemicalName(),contaminantData.getContaminationType(),contaminantData.getContaminationValue(),"Y"));
    			}
    			logger.info("Data Saved>"+res);
    		}
    	}
    	logger.info("Saved Data Result::"+result.toString());
    	return "Site data updated successfully";
    }
    
    @PostMapping("/getSiteData")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public SiteRegisterRequest getSiteData(@RequestBody SiteRegisterRequest siteRegisterRequest) {		
		
    	Site result = siteDataRepository.findBySiteId(siteRegisterRequest.getSiteId());
    	
    	siteRegisterRequest.setOrgName(result.getSiteOrg());
    	siteRegisterRequest.setSiteLocation(result.getSiteLocation());
    	siteRegisterRequest.setSiteName(result.getSiteName());
    	siteRegisterRequest.setState(result.getSiteState());
    	
    	List<SiteCalculation> siteContamiData = siteCalculationRepository.findBySiteId(siteRegisterRequest.getSiteId());
    	
    	List<SiteContaminantData> contaLst = new ArrayList<SiteContaminantData>();
    	SiteContaminantData e = null;
    	for(SiteCalculation siteCalc:siteContamiData) {
    		e = new SiteContaminantData();
    		e.setActiveYN(siteCalc.getActiveYN());
    		e.setId(siteCalc.getId());
    		e.setContaminationType(siteCalc.getContaminationType());
    		e.setChemicalName(siteCalc.getChemicalName());
    		e.setContaminationValue(siteCalc.getContaminationValue());
			contaLst.add(e);
    	}
    	siteRegisterRequest.setSiteContaminant(contaLst);
    	return siteRegisterRequest;
    }
    
}