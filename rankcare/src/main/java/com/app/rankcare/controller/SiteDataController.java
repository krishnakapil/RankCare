package com.app.rankcare.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import com.app.rankcare.model.*;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.app.rankcare.payload.ApiResponse;
import com.app.rankcare.payload.SiteContaminantData;
import com.app.rankcare.payload.SiteRegisterRequest;
import com.app.rankcare.payload.SiteResponse;
import com.app.rankcare.repository.SiteCalculationRepository;
import com.app.rankcare.repository.SiteDataRepository;
import com.app.rankcare.repository.ToxicityRepository;

@RestController
@RequestMapping("/api")
public class SiteDataController {

    @Autowired
    private SiteDataRepository siteDataRepository;

    @Autowired
    private ToxicityRepository toxicityRepository;

    @Autowired
    private SiteCalculationRepository siteCalculationRepository;
    @Autowired
    private ChemicalController chemicalController;
    @Autowired
    private ConsumptionController consumptionController;

    private static final Logger logger = LoggerFactory.getLogger(SiteDataController.class);

    @PostMapping("/site/add")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Object> registerSite(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {
        PlaceDetail placeDetail = getPlaceDetail(siteRegisterRequest.getSiteLocation());

        if (placeDetail == null) {
            throw new Exception("Please enter a valid address");
        }

        Site result = siteDataRepository.save(new Site(siteRegisterRequest.getProjectId(),
                siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(), siteRegisterRequest.getOrgName(), placeDetail.getLat(), placeDetail.getLng()));
        if (siteRegisterRequest.getSiteContaminant() != null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
            SiteCalculation res;
            for (SiteContaminantData contaminantData : siteRegisterRequest.getSiteContaminant()) {
                res = siteCalculationRepository.save(new SiteCalculation(result.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), "Y", contaminantData.getContaminationValueSd()));
                logger.info("Data Saved>" + res);
            }
        }
        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Site registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/site/update")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Object> updateSiteRegistration(@Valid @RequestBody SiteRegisterRequest siteRegisterRequest) throws Exception {
        if (siteRegisterRequest.getId() == null || siteRegisterRequest.getId() <= 0L) {
            throw new Exception("Id cannot be null or empty");
        }

        PlaceDetail placeDetail = getPlaceDetail(siteRegisterRequest.getSiteLocation());

        if (placeDetail == null) {
            throw new Exception("Please enter a valid address");
        }

        Site result = siteDataRepository.save(new Site(siteRegisterRequest.getId(), siteRegisterRequest.getProjectId(),
                siteRegisterRequest.getSiteName(), siteRegisterRequest.getSiteLocation(), siteRegisterRequest.getOrgName(), placeDetail.getLat(), placeDetail.getLng()));
        if (siteRegisterRequest.getSiteContaminant() != null && !siteRegisterRequest.getSiteContaminant().isEmpty()) {
            SiteCalculation res;
            for (SiteContaminantData contaminantData : siteRegisterRequest.getSiteContaminant()) {
                if (contaminantData.getId() != null) {
                    res = siteCalculationRepository.save(new SiteCalculation(contaminantData.getId(), siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), contaminantData.getActiveYN(), contaminantData.getContaminationValueSd()));
                } else {
                    res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), "Y", contaminantData.getContaminationValueSd()));
                }
                logger.info("Data Saved>" + res);
            }
        }
        logger.info("Saved Data Result::" + result.toString());
        return new ResponseEntity<Object>(new ApiResponse(true, "Site updated successfully"), HttpStatus.OK);
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
            @RequestParam(name = "count", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "projectId", defaultValue = "-1") Long projectId
    ) {
        Map<String, Object> resMap = new HashMap<>();

        if (projectId <= 0) {
            return resMap;
        }

        Pageable pagination = PageRequest.of(pageNo, pageSize);
        resMap.put("pageCnt", 0);
        Page<Site> pgLst = siteDataRepository.findSitesByProject(projectId, pagination);
        if (pgLst.hasContent()) {
            resMap.put("pageCnt", pgLst.getTotalPages());
            List<Site> siteList = pgLst.getContent();
            List<SiteResponse> updatedList = new ArrayList<>();

            for (Site site : siteList) {
                SiteResponse siteResponse = new SiteResponse(site, siteCalculationRepository.findBySiteId(site.getId()));
                updatedList.add(siteResponse);
            }

            resMap.put("data", updatedList);
        }

        return resMap;
    }

    @GetMapping("/location/autocomplete")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public List<Place> getLocations(@RequestParam(name = "query", defaultValue = "") String query) {
        final String uri = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=" + System.getenv("GOOGLE_API_KEY") + "&input=" + query;
        RestTemplate restTemplate = new RestTemplate();
        AutoCompleteResponse result = restTemplate.getForObject(uri, AutoCompleteResponse.class);
        return result.predictions;
    }

    private PlaceDetail getPlaceDetail(String location) {
        try {
            final String uri = "https://maps.googleapis.com/maps/api/place/textsearch/json?key=" + System.getenv("GOOGLE_API_KEY") + "&query=" + URLEncoder.encode(location, "UTF-8");
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, PlaceDetail.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @GetMapping("/site/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<SiteRegisterRequest> getSitesWithdata(@PathVariable("id") Long id) throws Exception {
        Optional<Site> siteOptional = siteDataRepository.findById(id);

        if (!siteOptional.isPresent()) {
            return null;
        }

        Site result = siteOptional.get();

        SiteRegisterRequest siteRegisterRequest = new SiteRegisterRequest();
        siteRegisterRequest.setId(id);
        siteRegisterRequest.setOrgName(result.getSiteOrg());
        siteRegisterRequest.setSiteLocation(result.getSiteLocation());
        siteRegisterRequest.setSiteName(result.getSiteName());
        siteRegisterRequest.setSiteLat(result.getSiteLat());
        siteRegisterRequest.setSiteLng(result.getSiteLng());

        List<SiteCalculation> siteContamiData = siteCalculationRepository.findBySiteId(id);

        List<SiteContaminantData> contaLst = new ArrayList<>();
        SiteContaminantData e = null;
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            for (SiteCalculation siteCalc : siteContamiData) {
                e = new SiteContaminantData();
                e.setActiveYN(siteCalc.getActiveYN());
                e.setId(siteCalc.getId());
                e.setContaminationType(siteCalc.getContaminationType());
                e.setChemicalId(siteCalc.getChemicalId());
                e.setContaminationValue(siteCalc.getContaminationValue());
                e.setMeasuringUnit(siteCalc.getMeasuringUnit());
                e.setValueWithUnit(siteCalc.getContaminationValue() + " " + siteCalc.getMeasuringUnit());
                e.setContaminationValueSd(siteCalc.getContaminationValueSd());
                Optional<Toxicity> toxicityOptional = toxicityRepository.findById(siteCalc.getChemicalId());
                if (toxicityOptional.isPresent()) {
                    e.setChemicalName(toxicityOptional.get().getChemicalName());
                }

                contaLst.add(e);
            }
        }

        Map<String, Map<String, Double>> t2 = siteCalculationT2(id);

        siteRegisterRequest.setSiteContaminant(contaLst);
        siteRegisterRequest.setT1(siteCalculationT1(id));
        siteRegisterRequest.setT3(siteCalculationT3(id));

        siteRegisterRequest.setT2(t2);

        return new ResponseEntity<>(siteRegisterRequest, HttpStatus.OK);
    }

    @PostMapping("/sites/data")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SiteRegisterRequest>> getSitesWithdata(@Valid @RequestBody List<Long> ids) throws Exception {
        List<Site> sites = siteDataRepository.findAllById(ids);
        List<SiteRegisterRequest> responseList = new ArrayList<>();

        for (Site result : sites) {
            Long id = result.getId();
            SiteRegisterRequest siteRegisterRequest = new SiteRegisterRequest();
            siteRegisterRequest.setId(id);
            siteRegisterRequest.setOrgName(result.getSiteOrg());
            siteRegisterRequest.setSiteLocation(result.getSiteLocation());
            siteRegisterRequest.setSiteName(result.getSiteName());
            siteRegisterRequest.setSiteLat(result.getSiteLat());
            siteRegisterRequest.setSiteLng(result.getSiteLng());

            List<SiteCalculation> siteContaminantData = siteCalculationRepository.findBySiteId(id);

            List<SiteContaminantData> contaLst = new ArrayList<>();
            SiteContaminantData e = null;
            if (siteContaminantData != null && !siteContaminantData.isEmpty()) {
                for (SiteCalculation siteCalc : siteContaminantData) {
                    e = new SiteContaminantData();
                    e.setActiveYN(siteCalc.getActiveYN());
                    e.setId(siteCalc.getId());
                    e.setContaminationType(siteCalc.getContaminationType());
                    e.setChemicalId(siteCalc.getChemicalId());
                    e.setContaminationValue(siteCalc.getContaminationValue());

                    Optional<Toxicity> toxicityOptional = toxicityRepository.findById(siteCalc.getChemicalId());
                    if (toxicityOptional.isPresent()) {
                        e.setChemicalName(toxicityOptional.get().getChemicalName());
                    }

                    contaLst.add(e);
                }
            }

            Map<String, Map<String, Double>> t2 = siteCalculationT2(id);

            siteRegisterRequest.setSiteContaminant(contaLst);
            siteRegisterRequest.setT1(siteCalculationT1(id));
            siteRegisterRequest.setT3(siteCalculationT3(id));

            siteRegisterRequest.setT2(t2);
            responseList.add(siteRegisterRequest);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/siteCalculations/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getSiteCalculations(@PathVariable("id") Long id) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("T1", siteCalculationT1(id));

        Map<String, Map<String, Double>> t2 = siteCalculationT2(id);
        resMap.put("T2", t2);

        resMap.put("T3", siteCalculationT3(id));
        return resMap;
    }

    public Map<String, Double> siteCalculationT1(Long id) {
        Map<Long, Toxicity> chemicalData = chemicalController.getChemicalsData();
        List<SiteCalculation> siteContamiData;
        Double tw = 0d;
        Double ts = 0d;
        Map<String, Double> siteT1Vals = new HashMap<>();
        siteContamiData = siteCalculationRepository.findBySiteId(id);
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            Toxicity t;
            for (SiteCalculation siteCalc : siteContamiData) {
                t = chemicalData.get(siteCalc.getChemicalId());
                if ("Water".equalsIgnoreCase(siteCalc.getContaminationType())) {
                    tw += (Double.valueOf(siteCalc.getContaminationValue())) / Double.valueOf(t.getWaterGuideline());
                } else if ("Soil".equalsIgnoreCase(siteCalc.getContaminationType())) {
                    ts += (Double.valueOf(siteCalc.getContaminationValue())) / Double.valueOf(t.getSoilGuideline());
                }
            }
        }
        siteT1Vals.put("Water", tw);
        siteT1Vals.put("Soil", ts);

        return siteT1Vals;
    }

    public Map<String, Map<String, Double>> siteCalculationT2(Long id) {
        Map<Long, Toxicity> chemicalData = chemicalController.getChemicalsData();
        Map<String, Consumption> consumptionData = consumptionController.getConsumptionAgeGrpData();
        List<SiteCalculation> siteContamiData;
        Map<String, Double> siteT2Vals;
        Map<String, Map<String, Double>> res = new LinkedHashMap<>();
        siteContamiData = siteCalculationRepository.findBySiteId(id);
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            for (String c : consumptionData.keySet()) {
                siteT2Vals = new HashMap<>();
                Toxicity t;
                Double val;
                Double ncr = 0d;
                Double cr = 0d;
                for (SiteCalculation siteCalc : siteContamiData) {
                    val = 0d;
                    t = chemicalData.get(siteCalc.getChemicalId());
                    if ("Water".equalsIgnoreCase(siteCalc.getContaminationType())) {
                        val = ((Double.valueOf(siteCalc.getContaminationValue())) * Double.valueOf(consumptionData.get(c).getWaterConsAvg()));
                    } else if ("Soil".equalsIgnoreCase(siteCalc.getContaminationType())) {
                        val = ((Double.valueOf(siteCalc.getContaminationValue())) * Double.valueOf(consumptionData.get(c).getSoilInvAvg()));
                    }

                    val = val / Double.valueOf(consumptionData.get(c).getBodyWtMean());

                    ncr += val / Double.valueOf(t.getDosageRefValue());
                    cr += val * Double.valueOf(t.getCancerSlopeFactor());
                }

                siteT2Vals.put("NCR", ncr);
                siteT2Vals.put("CR", cr);
                res.put(c, siteT2Vals);
            }
        }
        return res;
    }

    private Map<String, Map<String, Double>> siteCalculationT3(Long id) {
        Map<Long, Toxicity> toxicityMap = chemicalController.getChemicalsData();
        Map<String, Consumption> consumptionData = consumptionController.getConsumptionAgeGrpData();
        List<SiteCalculation> siteCalculationData = siteCalculationRepository.findBySiteId(id);

        Map<String, Map<String, Double>> res = new LinkedHashMap<>();

        if (siteCalculationData != null && !siteCalculationData.isEmpty()) {
            for (String ageGrp : consumptionData.keySet()) {
                Map<String, Double> siteT3Vals = new HashMap<>();
                Double ncr = 0d;
                Double cr = 0d;

                double soilGeoMean = Double.valueOf(consumptionData.get(ageGrp).getSoilInvGomMean());
                double waterGeoMean = Double.valueOf(consumptionData.get(ageGrp).getWaterInvGomMean());
                double soilGeoSd = Double.valueOf(consumptionData.get(ageGrp).getSoilInvGomSd());
                double waterGeoSd = Double.valueOf(consumptionData.get(ageGrp).getWaterInvGomSd());
                double bodyWtMean = Double.valueOf(consumptionData.get(ageGrp).getBodyWtMean());
                double bodyWtSd = Double.valueOf(consumptionData.get(ageGrp).getBodyWtSd());

                Map<Long, SiteChemicalData> siteChemicalDataMap = new HashMap<>();

                for (SiteCalculation siteCalculation : siteCalculationData) {
                    SiteChemicalData siteChemicalData;

                    if (siteChemicalDataMap.containsKey(siteCalculation.getChemicalId())) {
                        siteChemicalData = siteChemicalDataMap.get(siteCalculation.getChemicalId());
                    } else {
                        siteChemicalData = new SiteChemicalData();
                    }

                    siteChemicalData.setToxicity(toxicityMap.get(siteCalculation.getChemicalId()));

                    if (siteCalculation.getContaminationType().equalsIgnoreCase("water")) {
                        siteChemicalData.setWaterMean(Double.parseDouble(siteCalculation.getContaminationValue()));
                        siteChemicalData.setWaterSd(Double.parseDouble(siteCalculation.getContaminationValueSd()));
                    } else {
                        siteChemicalData.setSoilMean(Double.parseDouble(siteCalculation.getContaminationValue()));
                        siteChemicalData.setSoilSd(Double.parseDouble(siteCalculation.getContaminationValueSd()));
                    }

                    siteChemicalDataMap.put(siteCalculation.getChemicalId(), siteChemicalData);
                }

                for (SiteChemicalData siteChemicalData : siteChemicalDataMap.values()) {
                    double aSoil = calculateAValue(siteChemicalData.getSoilMean(), siteChemicalData.getSoilSd());
                    double aWater = calculateAValue(siteChemicalData.getSoilMean(), siteChemicalData.getSoilSd());
                    double b = calculateLogNrnd(Math.log(soilGeoMean), Math.log(soilGeoSd)) / Math.pow(10, 6);
                    double c = calculateLogNrnd(Math.log(waterGeoMean), Math.log(waterGeoSd));
                    double d = calculateNormrnd(bodyWtMean, bodyWtSd);

                    double value = (aSoil * b / d) + (aWater * c / d);
                    double crValue = (value / Double.parseDouble(siteChemicalData.getToxicity().getCancerSlopeFactor()));
                    double ncrValue = (value / Double.parseDouble(siteChemicalData.getToxicity().getDosageRefValue()));

                    cr += crValue;
                    ncr += ncrValue;
                }


                siteT3Vals.put("NCR", Double.isNaN(ncr) ? 0 : ncr);
                siteT3Vals.put("CR", Double.isNaN(cr) ? 0 : cr);
                res.put(ageGrp, siteT3Vals);
            }
        }

        return res;
    }

    private double calculateAValue(double mean, double sd) {
        double pow2 = Math.pow(mean, 2);
        double mu = Math.log(pow2 / Math.sqrt(sd + pow2));
        double sigma = Math.sqrt(Math.log(sd / pow2 + 1));

        return calculateLogNrnd(mu, sigma);
    }

    private double calculateLogNrnd(double scale, double shape) {
        if (scale == 0 && shape == 0) {
            return 0;
        }

        double val = 0;

        LogNormalDistribution logNormalDistribution = new LogNormalDistribution(scale, shape, 1);

        for (int i = 0; i < 5000; i++) {
            double ran = logNormalDistribution.sample();
            val += ran;
        }

        return val / 5000.0;
    }

    private double calculateNormrnd(double scale, double shape) {
        if (scale == 0 && shape == 0) {
            return 0;
        }

        double val = 0;

        NormalDistribution normalDistribution = new NormalDistribution(scale, shape, 1);

        for (int i = 0; i < 5000; i++) {
            val += normalDistribution.sample();
        }

        return val / 5000.0;
    }
}