package com.app.rankcare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.Site;
import com.app.rankcare.model.SiteCalculation;
import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.SiteContaminantData;
import com.app.rankcare.payload.SiteRegisterRequest;
import com.app.rankcare.payload.ToxicityRequest;
import com.app.rankcare.repository.ToxicityRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	 @Autowired
	 private ToxicityRepository toxicityRepository;
	 @Autowired
	 EntityManager entityManager;
	 
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
	 public Map<String,Object> getToxicityPagination(@RequestParam(defaultValue="0") Integer pageNo,@RequestParam(defaultValue="10") Integer pageSize,@RequestParam(defaultValue="id") String sortBy,@RequestParam String searchBy) {		
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
	 @PostMapping("/getToxicityPaginationSearch")			
	 public Map<String,Object> getToxicityPaginationSearch(@RequestBody ToxicityRequest toxicReq) {		
		 Map<String,Object> resMap = new HashMap<String,Object>();
		 resMap.put("pageCnt",0);
		 resMap.put("data",new ArrayList<Toxicity>());
		
		  final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		  final CriteriaQuery<Toxicity> criteriaQuery = criteriaBuilder.createQuery(Toxicity.class);
		  final Root<Toxicity> employeeRoot = criteriaQuery.from(Toxicity.class);
		
		  List<Predicate> criteriaList = new ArrayList<Predicate>();
		  Map<String,String> searchMap = toxicReq.getSearchBy();
		  if(searchMap!=null && !searchMap.isEmpty()) {
			  for (String str:searchMap.keySet()) {
		         // Predicate predicateSearch = criteriaBuilder.like(criteriaBuilder.upper(employeeRoot.get(str).as(String.class)), "%" + searchMap.get(str) + "%");
				  Predicate predicateSearch = criteriaBuilder.equal(criteriaBuilder.upper(employeeRoot.get(str).as(String.class)), searchMap.get(str));
		          criteriaList.add(predicateSearch);
			  }
		  }
		  if (!StringUtils.isEmpty(toxicReq.getSortBy())) {
	          if (!StringUtils.isEmpty(toxicReq.getOrderBy()) && toxicReq.getOrderBy().equalsIgnoreCase("DESC"))
	             criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(toxicReq.getSortBy())));
	          else
	             criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(toxicReq.getSortBy())));
		  }
		  criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
 
		  final TypedQuery<Toxicity> query = entityManager.createQuery(criteriaQuery);
		  List<Toxicity> res = query.getResultList();
		  if(res!=null && !res.isEmpty())
		  {
			  int count = query.getResultList().size();
			  query.setFirstResult((toxicReq.getPageNo()-1)*toxicReq.getPageSize());
			  query.setMaxResults(toxicReq.getPageSize());
			  if(count<toxicReq.getPageSize()) {
				resMap.put("pageCnt",1);
			  }else{ 
				int d= (count/toxicReq.getPageSize()); 
				int r=(count%toxicReq.getPageSize()); 
				if(r>0) {
					d+=1;
				}
				resMap.put("pageCnt",d);
			  } 
			  resMap.put("data",query.getResultList());
		  }
		 
		  return resMap;
	 }
	@PostMapping("/updateToxicity")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {		
		
    	Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getId(),toxicityRequest.getChemicalName(),toxicityRequest.getChemicalFormula(),
    			toxicityRequest.getSoilGuideline(),toxicityRequest.getSoilRef(),toxicityRequest.getWaterGuideline(),toxicityRequest.getWaterRef(),
    			toxicityRequest.getDosageRef(),toxicityRequest.getReference(),toxicityRequest.getCancerSlopeFactor(),toxicityRequest.getCancerSlopeRef()));
   
    	logger.info("Saved Data Result::"+result.toString());
    	return "Toxicity data updated successfully";
    }

	@PostMapping("/insertToxicity")
    @PreAuthorize("hasRole('ADMIN')")
    public String insertToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {		
    	Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getChemicalName(),toxicityRequest.getChemicalFormula(),
    			toxicityRequest.getSoilGuideline(),toxicityRequest.getSoilRef(),toxicityRequest.getWaterGuideline(),toxicityRequest.getWaterRef(),
    			toxicityRequest.getDosageRef(),toxicityRequest.getReference(),toxicityRequest.getCancerSlopeFactor(),toxicityRequest.getCancerSlopeRef()));
   
    	logger.info("Saved Data Result::"+result.toString());
    	return "Toxicity data saved successfully";
    }
	
}
