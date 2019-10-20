package com.app.rankcare.controller;

import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.ChemicalsResponse;
import com.app.rankcare.payload.ToxicityRequest;
import com.app.rankcare.repository.ToxicityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChemicalController {
    @Autowired
    private ToxicityRepository toxicityRepository;

    @Autowired
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ChemicalController.class);

    @GetMapping("/chemicals")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public List<ChemicalsResponse> getToxicityChemicals() {
        List<ChemicalsResponse> chemicals = new ArrayList<>();
        List<Toxicity> toxicData = toxicityRepository.findAll();
        if (toxicData != null && !toxicData.isEmpty()) {
            for (Toxicity t : toxicData) {
                chemicals.add(new ChemicalsResponse(t.getId(), t.getChemicalName()));
            }
        }
        return chemicals;
    }

    @PostMapping("/chemicals/search")
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
}
