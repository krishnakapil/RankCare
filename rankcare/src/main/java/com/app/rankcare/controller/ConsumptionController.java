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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.rankcare.model.Consumption;
import com.app.rankcare.payload.ApiResponse;
import com.app.rankcare.payload.ConsumptionRequest;
import com.app.rankcare.repository.ConsumptionRepository;

@RestController
@RequestMapping("/api")
public class ConsumptionController {
    @Autowired
    private ConsumptionRepository consumptionRepository;
    @Autowired
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionController.class);

    public List<Consumption> getAllConsumptionData() {
        try {
            return consumptionRepository.findAll();
        } catch (Exception e) {
            logger.error("Fetching of Consumptiondata error:" + e.getMessage());
        }
        return null;
    }


    @GetMapping("/consumption")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getConsumptionPagination(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "count", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
    ) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        resMap.put("pageCnt", 0);
        resMap.put("data", new ArrayList<Consumption>());
        Page<Consumption> pgLst = consumptionRepository.findAll(pagination);
        List<Consumption> res = pgLst.getContent();

        if (pgLst.hasContent()) {
            resMap.put("pageCnt", pgLst.getTotalPages());

            for (Consumption c : res) {
                String ageGrp = c.getAgeGrp();

                if (ageGrp != null && ageGrp.contains("-")) {
                    String[] ageGrpSplit = ageGrp.split("-");
                    if (ageGrpSplit.length == 2) {
                        c.setAgeFrom(Integer.parseInt(ageGrpSplit[0]));
                        c.setAgeTo(Integer.parseInt(ageGrpSplit[1]));
                    } else if (ageGrpSplit.length == 1) {
                        c.setAgeFrom(Integer.parseInt(ageGrpSplit[0]));
                        c.setAgeTo(null);
                    }
                }
            }

            resMap.put("data", res);
        }
        return resMap;
    }

    @PostMapping("/consumption/search")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getConsumptionPaginationSearch(@RequestBody ConsumptionRequest consumptionReq) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("pageCnt", 0);
        resMap.put("data", new ArrayList<Consumption>());

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Consumption> criteriaQuery = criteriaBuilder.createQuery(Consumption.class);
        final Root<Consumption> employeeRoot = criteriaQuery.from(Consumption.class);

        List<Predicate> criteriaList = new ArrayList<Predicate>();
        Map<String, String> searchMap = consumptionReq.getSearchBy();
        if (searchMap != null && !searchMap.isEmpty()) {
            for (String str : searchMap.keySet()) {
                // Predicate predicateSearch = criteriaBuilder.like(criteriaBuilder.upper(employeeRoot.get(str).as(String.class)), "%" + searchMap.get(str) + "%");
                Predicate predicateSearch = criteriaBuilder.equal(criteriaBuilder.upper(employeeRoot.get(str).as(String.class)), searchMap.get(str));
                criteriaList.add(predicateSearch);
            }
        }
        if (!StringUtils.isEmpty(consumptionReq.getSortBy())) {
            if (!StringUtils.isEmpty(consumptionReq.getOrderBy()) && consumptionReq.getOrderBy().equalsIgnoreCase("DESC"))
                criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(consumptionReq.getSortBy())));
            else
                criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(consumptionReq.getSortBy())));
        }
        criteriaQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));

        final TypedQuery<Consumption> query = entityManager.createQuery(criteriaQuery);
        List<Consumption> res = query.getResultList();
        if (res != null && !res.isEmpty()) {
            int count = query.getResultList().size();
            query.setFirstResult((consumptionReq.getPageNo() - 1) * consumptionReq.getPageSize());
            query.setMaxResults(consumptionReq.getPageSize());
            if (count < consumptionReq.getPageSize()) {
                resMap.put("pageCnt", 1);
            } else {
                int d = (count / consumptionReq.getPageSize());
                int r = (count % consumptionReq.getPageSize());
                if (r > 0) {
                    d += 1;
                }
                resMap.put("pageCnt", d);
            }

            for (Consumption c : res) {
                String ageGrp = c.getAgeGrp();

                if (ageGrp != null && ageGrp.contains("-")) {
                    String[] ageGrpSplit = ageGrp.split("-");
                    if (ageGrpSplit.length == 2) {
                        c.setAgeFrom(Integer.parseInt(ageGrpSplit[0]));
                        c.setAgeTo(Integer.parseInt(ageGrpSplit[1]));
                    } else if (ageGrpSplit.length == 1) {
                        c.setAgeFrom(Integer.parseInt(ageGrpSplit[0]));
                        c.setAgeTo(null);
                    }
                }
            }
            resMap.put("data", res);
        }

        return resMap;
    }

    @PostMapping("/consumption/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateConsumption(@Valid @RequestBody ConsumptionRequest consumptionRequest) {
        String ageGrp = consumptionRequest.getAgeFrom() + "-";

        if(consumptionRequest.getAgeTo() != null) {
            ageGrp += consumptionRequest.getAgeTo();
        }

        Consumption result = consumptionRepository.save(new Consumption(consumptionRequest.getId(), ageGrp, consumptionRequest.getBodyWtAvg(), consumptionRequest.getCiData1(),
                consumptionRequest.getSoilInvAvg(), consumptionRequest.getWaterConsAvg(), consumptionRequest.getCiData2()));

        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Consumption data updated successfully"), HttpStatus.OK);
    }

    @PostMapping("/consumption/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> insertConsumption(@Valid @RequestBody ConsumptionRequest consumptionRequest) {
        String ageGrp = consumptionRequest.getAgeFrom() + "-";

        if(consumptionRequest.getAgeTo() != null) {
            ageGrp += consumptionRequest.getAgeTo();
        }

        Consumption result = consumptionRepository.save(new Consumption(ageGrp, consumptionRequest.getBodyWtAvg(), consumptionRequest.getCiData1(),
                consumptionRequest.getSoilInvAvg(), consumptionRequest.getWaterConsAvg(), consumptionRequest.getCiData2()));

        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Consumption data saved successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/consumption/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) throws Exception {
        consumptionRepository.deleteById(id.longValue());
        return new ResponseEntity<Object>(new ApiResponse(true, "Consumption data deleted successfully!"), HttpStatus.OK);
    }
}
