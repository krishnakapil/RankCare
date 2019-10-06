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

import com.app.rankcare.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.ToxicityRequest;
import com.app.rankcare.repository.ToxicityRepository;

@RestController
@RequestMapping("/api")
public class ToxicityController {
    @Autowired
    private ToxicityRepository toxicityRepository;
    @Autowired
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ToxicityController.class);

    public List<Toxicity> getAllToxicityData() {
        try {
            return toxicityRepository.findAll();
        } catch (Exception e) {
            logger.error("Fetching of toxicitydata error:" + e.getMessage());
        }
        return null;
    }

    @GetMapping("/toxicity/all")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<Long, String> getToxicityChemicals() {
        Map<Long, String> chemicalMap = new HashMap<Long, String>();
        List<Toxicity> toxicData = getAllToxicityData();
        if (toxicData != null && !toxicData.isEmpty()) {
            for (Toxicity t : toxicData) {
                chemicalMap.put(t.getId(), t.getChemicalName());
            }
        }
        return chemicalMap;
    }

    @GetMapping("/toxicity")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getToxicityPagination(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "count", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
    ) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        resMap.put("pageCnt", 0);
        resMap.put("data", new ArrayList<Toxicity>());
        Page<Toxicity> pgLst = toxicityRepository.findAll(pagination);
        if (pgLst.hasContent()) {
            resMap.put("pageCnt", pgLst.getTotalPages());
            resMap.put("data", pgLst.getContent());
        }
        return resMap;
    }

    @PostMapping("/toxicity/search")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getToxicityPaginationSearch(@RequestBody ToxicityRequest toxicReq) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("pageCnt", 0);
        resMap.put("data", new ArrayList<Toxicity>());

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Toxicity> criteriaQuery = criteriaBuilder.createQuery(Toxicity.class);
        final Root<Toxicity> employeeRoot = criteriaQuery.from(Toxicity.class);

        List<Predicate> criteriaList = new ArrayList<Predicate>();
        Map<String, String> searchMap = toxicReq.getSearchBy();
        if (searchMap != null && !searchMap.isEmpty()) {
            for (String str : searchMap.keySet()) {
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
        if (res != null && !res.isEmpty()) {
            int count = query.getResultList().size();
            query.setFirstResult((toxicReq.getPageNo() - 1) * toxicReq.getPageSize());
            query.setMaxResults(toxicReq.getPageSize());
            if (count < toxicReq.getPageSize()) {
                resMap.put("pageCnt", 1);
            } else {
                int d = (count / toxicReq.getPageSize());
                int r = (count % toxicReq.getPageSize());
                if (r > 0) {
                    d += 1;
                }
                resMap.put("pageCnt", d);
            }
            resMap.put("data", query.getResultList());
        }

        return resMap;
    }

    @PostMapping("/toxicity/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {
        Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getId(), toxicityRequest.getChemicalName(), toxicityRequest.getChemicalFormula(),
                toxicityRequest.getSoilGuideline(), toxicityRequest.getSoilRef(), toxicityRequest.getWaterGuideline(), toxicityRequest.getWaterRef(),
                toxicityRequest.getDosageRef(), toxicityRequest.getReference(), toxicityRequest.getCancerSlopeFactor(), toxicityRequest.getCancerSlopeRef()));

        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity data updated successfully"), HttpStatus.OK);
    }

    @PostMapping("/toxicity/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> insertToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {
        Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getChemicalName(), toxicityRequest.getChemicalFormula(),
                toxicityRequest.getSoilGuideline(), toxicityRequest.getSoilRef(), toxicityRequest.getWaterGuideline(), toxicityRequest.getWaterRef(),
                toxicityRequest.getDosageRef(), toxicityRequest.getReference(), toxicityRequest.getCancerSlopeFactor(), toxicityRequest.getCancerSlopeRef()));

        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity data saved successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/toxicity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) throws Exception {
        toxicityRepository.deleteById(id.longValue());
        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity deleted successfully!"), HttpStatus.OK);
    }
}
