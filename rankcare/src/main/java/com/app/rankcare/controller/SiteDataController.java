package com.app.rankcare.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.app.rankcare.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.rankcare.model.Consumption;
import com.app.rankcare.model.Site;
import com.app.rankcare.model.SiteCalculation;
import com.app.rankcare.model.Toxicity;
import com.app.rankcare.payload.SiteContaminantData;
import com.app.rankcare.payload.SiteRegisterRequest;
import com.app.rankcare.repository.SiteCalculationRepository;
import com.app.rankcare.repository.SiteDataRepository;

@RestController
@RequestMapping("/api")
public class SiteDataController {

    @Autowired
    private SiteDataRepository siteDataRepository;


    @Autowired
    private SiteCalculationRepository siteCalculationRepository;
    @Autowired
    private ChemicalController chemicalController;
    @Autowired
    private ConsumptionController consumptionController;
    
    

    private static final Logger logger = LoggerFactory.getLogger(SiteDataController.class);

    @PostMapping("/site/add")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Object> registerSite(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) {

        Site result = siteDataRepository.save(new Site(siteRegisterRequest.getSiteId(),
                siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
                siteRegisterRequest.getState(), siteRegisterRequest.getOrgName()));
        if (siteRegisterRequest.getSiteContaminant() != null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
            SiteCalculation res;
            for (SiteContaminantData contaminantData : siteRegisterRequest.getSiteContaminant()) {
                res = siteCalculationRepository.save(new SiteCalculation(result.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), "Y"));
                logger.info("Data Saved>" + res);
            }
        }
        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Site registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/site/update")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<String> updateSiteRegistration(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {
        if (siteRegisterRequest.getId() == null || siteRegisterRequest.getId() <= 0L) {
            throw new Exception("Id cannot be null or empty");
        }
        Site result = siteDataRepository.save(new Site(siteRegisterRequest.getId(), siteRegisterRequest.getSiteId(),
                siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
                siteRegisterRequest.getState(), siteRegisterRequest.getOrgName()));
        if (siteRegisterRequest.getSiteContaminant() != null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
            SiteCalculation res;
            for (SiteContaminantData contaminantData : siteRegisterRequest.getSiteContaminant()) {
                if (contaminantData.getId() != null) {
                    res = siteCalculationRepository.save(new SiteCalculation(contaminantData.getId(), siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getActiveYN()));
                } else {
                    res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), "Y"));
                }
                logger.info("Data Saved>" + res);
            }
        }
        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<String>("Site data updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) throws Exception {
        siteDataRepository.deleteById(id.longValue());
        return new ResponseEntity<Object>(new ApiResponse(true, "Site deleted successfully!"), HttpStatus.OK);
    }

    @GetMapping("/sites")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getSitesPagination(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "count", defaultValue = "10") Integer pageSize
    ) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        Pageable pagination = PageRequest.of(pageNo, pageSize);
        resMap.put("pageCnt", 0);
        resMap.put("data", new ArrayList<Toxicity>());
        Page<Site> pgLst = siteDataRepository.findAll(pagination);
        if (pgLst.hasContent()) {
            resMap.put("pageCnt", pgLst.getTotalPages());
            resMap.put("data", pgLst.getContent());
        }
        return resMap;
    }

//    @PostMapping("/updateSiteRegistration")
//    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
//    public ResponseEntity<String> updateSiteRegistrationData(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {
//        if (siteRegisterRequest.getId() == null || siteRegisterRequest.getId() <= 0L) {
//            throw new Exception("Id cannot be null or empty");
//        }
//        Site result = siteDataRepository.save(new Site(siteRegisterRequest.getId(), siteRegisterRequest.getSiteId(),
//                siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(),
//                siteRegisterRequest.getState(), siteRegisterRequest.getOrgName()));
//        logger.info("Saved Data Result::" + result.toString());
//        return new ResponseEntity<String>("Site Registration data updated successfully", HttpStatus.OK);
//    }
//
//    @PostMapping("/saveSiteContaminant")
//    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
//    public ResponseEntity<String> saveSiteContaminantData(@RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {
//        if (siteRegisterRequest.getId() == null || siteRegisterRequest.getId() <= 0L) {
//            throw new Exception("Id cannot be null or empty");
//        }
//        if (siteRegisterRequest.getSiteContaminant() != null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
//            SiteCalculation res;
//            SiteContaminantData contaminantData = siteRegisterRequest.getSiteContaminant().get(0);
//            if (contaminantData.getId() != null) {
//                res = siteCalculationRepository.save(new SiteCalculation(contaminantData.getId(), siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getActiveYN()));
//            } else {
//                res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), "Y"));
//            }
//            logger.info("Data Saved>" + res);
//        }
//        return new ResponseEntity<String>("Site Contaminant data updated successfully", HttpStatus.OK);
//    }

    @PostMapping("/getSiteData")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<SiteRegisterRequest> getSiteData(@RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {

        //Site result = siteDataRepository.findBySiteId(siteRegisterRequest.getSiteId());
        Optional<Site> siteDtl = siteDataRepository.findById(siteRegisterRequest.getId());
        if (!siteDtl.isPresent()) {
            throw new Exception("Site Data Not Available");
        }
        Site result = siteDtl.get();
        siteRegisterRequest.setOrgName(result.getSiteOrg());
        siteRegisterRequest.setSiteLocation(result.getSiteLocation());
        siteRegisterRequest.setSiteName(result.getSiteName());
        siteRegisterRequest.setState(result.getSiteState());

        List<SiteCalculation> siteContamiData = siteCalculationRepository.findBySiteId(siteRegisterRequest.getId());

        List<SiteContaminantData> contaLst = new ArrayList<SiteContaminantData>();
        SiteContaminantData e = null;
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            for (SiteCalculation siteCalc : siteContamiData) {
                e = new SiteContaminantData();
                e.setActiveYN(siteCalc.getActiveYN());
                e.setId(siteCalc.getId());
                e.setContaminationType(siteCalc.getContaminationType());
                e.setChemicalId(siteCalc.getChemicalId());
                e.setContaminationValue(siteCalc.getContaminationValue());
                contaLst.add(e);
            }
        }
        siteRegisterRequest.setSiteContaminant(contaLst);
        return new ResponseEntity<SiteRegisterRequest>(siteRegisterRequest, HttpStatus.OK);
    }


    public Map<String, Double> siteCalculationT1(int id) throws Exception {
        Map<Long,Toxicity> chemicalData=chemicalController.getChemicalsData();
        List<SiteCalculation> siteContamiData = null;
        Double tw=0d;
        Double ts=0d;
        Map<String, Double> siteT1Vals = new HashMap<String, Double>();
        siteContamiData = siteCalculationRepository.findBySiteId(Long.valueOf(id));
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            Toxicity t=null;
            for (SiteCalculation siteCalc : siteContamiData) {
                t=chemicalData.get(siteCalc.getChemicalId());
                if("Water".equalsIgnoreCase(siteCalc.getContaminationType())){
                    tw+=Double.valueOf(siteCalc.getContaminationValue())/Double.valueOf(t.getWaterGuideline());
                }
                else if("Soil".equalsIgnoreCase(siteCalc.getContaminationType())){
                    ts+=Double.valueOf(siteCalc.getContaminationValue())/Double.valueOf(t.getSoilGuideline());
                }
            }
        }
        siteT1Vals.put("Water",tw);
        siteT1Vals.put("Soil",ts);

        return siteT1Vals;
    }
    
    public Map<String, Map<String, Double>> siteCalculationT2(Integer id) throws Exception {
        Map<Long,Toxicity> chemicalData=chemicalController.getChemicalsData();
        Map<String,Consumption> consumptionData=consumptionController.getConsumptionAgeGrpData();
        List<SiteCalculation> siteContamiData = null;
        Map<String, Double> siteT2Vals = new HashMap<String,Double>();
        Map<String,Map<String,Double>> res = new HashMap<String,Map<String,Double>>();
        siteContamiData = siteCalculationRepository.findBySiteId(Long.valueOf(id));
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            for(String c:consumptionData.keySet()) {
                Toxicity t=null;
                Double val;
                Double ncr=0d;
                Double cr=0d;
                for (SiteCalculation siteCalc : siteContamiData) {
                    val=0d;
                    t=chemicalData.get(siteCalc.getChemicalId());
                    if("Water".equalsIgnoreCase(siteCalc.getContaminationType())){
                        val=Double.valueOf(siteCalc.getContaminationValue())*Double.valueOf(consumptionData.get(c).getWaterConsAvg());
                    }
                    else if("Soil".equalsIgnoreCase(siteCalc.getContaminationType())){
                        val =Double.valueOf(siteCalc.getContaminationValue())*Double.valueOf(consumptionData.get(c).getSoilInvAvg());
                    }
                    ncr+=val/Double.valueOf(t.getDosageRef());
                    cr+=val*Double.valueOf(t.getCancerSlopeFactor());
                }
                siteT2Vals.put("NCR",ncr);
                siteT2Vals.put("CR",cr);
                res.put(c,siteT2Vals);
            }
        }
        return res;
    }
    @GetMapping("/siteCalculations/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getSiteCalculations(@PathVariable("id") Integer id) throws Exception {
        Map<String,Object> resMap =  new HashMap<String,Object>();
        resMap.put("T1",siteCalculationT1(id));
        resMap.put("T2",siteCalculationT2(id));
        return resMap;
    }
}