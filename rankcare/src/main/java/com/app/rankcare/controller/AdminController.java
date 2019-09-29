package com.app.rankcare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.ToxicityRequest;
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
	 @GetMapping("/getToxicityPagination")			
	 public Map<String,Object> getToxicityPagination(@RequestParam(defaultValue="0") Integer pageNo,@RequestParam(defaultValue="10") Integer pageSize,@RequestParam(defaultValue="id") String sortBy) {		
		 Map<String,Object> resMap = new HashMap<String,Object>();
		 Pageable pagination= PageRequest.of(pageNo,pageSize,Sort.by(sortBy));
		 resMap.put("pageCnt",0);
		 resMap.put("data",new ArrayList<Toxicity>());
		 Page<Toxicity> pgLst=toxicityRepository.findAll(pagination);
		 if(pgLst.hasContent()) {	
			 resMap.put("pageCnt",pgLst.getTotalPages());
			 resMap.put("data",pgLst.getContent());
		 }
	    return resMap;
	 }
	 

}
