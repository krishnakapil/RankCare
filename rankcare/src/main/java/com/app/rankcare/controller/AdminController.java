package com.app.rankcare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.Toxicity;
import com.app.rankcare.repository.ToxicityRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	 @Autowired
	 private ToxicityRepository toxicityRepository;
	 
	 private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	 
	 public List<Toxicity> getAllToxicityData() {
		 try {
		    return toxicityRepository.findAll();
		 }catch(Exception e) {
			 logger.error("Fetching of toxicitydata error:"+e.getMessage());
		 }
		return null;
	 } 
	 @GetMapping("/getToxicityChemicals")			
	 public Map<Long,String> getToxicityChemicals() {		
		Map<Long,String> chemicalMap = new HashMap<Long,String>(); 
	    List<Toxicity> toxicData = getAllToxicityData();
	    if(toxicData!=null && !toxicData.isEmpty()) {
	    	for(Toxicity t: toxicData) {
	    		chemicalMap.put(t.getId(), t.getChemicalName());
	    	}
	    }
	    return chemicalMap;
	 }

}
