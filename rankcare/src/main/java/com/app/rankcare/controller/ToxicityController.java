package com.app.rankcare.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import com.app.rankcare.payload.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.ToxicityRequest;
import com.app.rankcare.repository.ToxicityRepository;

@RestController
@RequestMapping("/api")
public class ToxicityController {
    @Autowired
    private ToxicityRepository toxicityRepository;

    private static final Logger logger = LoggerFactory.getLogger(ToxicityController.class);

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

    @PostMapping("/toxicity/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {
        try {
            Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getId(), toxicityRequest.getChemicalName(), toxicityRequest.getChemicalFormula(),
                    toxicityRequest.getSoilGuideline(), toxicityRequest.getSoilRef(), toxicityRequest.getWaterGuideline(), toxicityRequest.getWaterRef(),
                    toxicityRequest.getCancerSlopeFactor(), toxicityRequest.getCancerSlopeRef()));

            logger.info("Saved Data Result::" + result.toString());
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Chemical name and Chemical formula should be unique"), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Something went wrong!"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity data updated successfully"), HttpStatus.OK);
    }

    @PostMapping("/toxicity/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> insertToxicity(@Valid @RequestBody ToxicityRequest toxicityRequest) {
        try {
            Toxicity result = toxicityRepository.save(new Toxicity(toxicityRequest.getChemicalName(), toxicityRequest.getChemicalFormula(),
                    toxicityRequest.getSoilGuideline(), toxicityRequest.getSoilRef(), toxicityRequest.getWaterGuideline(), toxicityRequest.getWaterRef(),
                    toxicityRequest.getCancerSlopeFactor(), toxicityRequest.getCancerSlopeRef()));

            logger.info("Saved Data Result::" + result.toString());
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Chemical name and Chemical formula should be unique"), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Something went wrong!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity data saved successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/toxicity/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) throws Exception {
        toxicityRepository.deleteById(id.longValue());
        return new ResponseEntity<Object>(new ApiResponse(true, "Toxicity deleted successfully!"), HttpStatus.OK);
    }
}
