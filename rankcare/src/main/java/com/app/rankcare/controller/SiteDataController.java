package com.app.rankcare.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.apache.commons.math3.distribution.LogNormalDistribution;
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

import com.app.rankcare.model.AutoCompleteResponse;
import com.app.rankcare.model.Consumption;
import com.app.rankcare.model.Place;
import com.app.rankcare.model.PlaceDetail;
import com.app.rankcare.model.Site;
import com.app.rankcare.model.SiteCalculation;
import com.app.rankcare.model.Toxicity;
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
                res = siteCalculationRepository.save(new SiteCalculation(result.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), "Y"));
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
                    res = siteCalculationRepository.save(new SiteCalculation(contaminantData.getId(), siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), contaminantData.getActiveYN()));
                } else {
                    res = siteCalculationRepository.save(new SiteCalculation(siteRegisterRequest.getId(), contaminantData.getChemicalId(), contaminantData.getContaminationType(), contaminantData.getContaminationValue(), contaminantData.getMeasuringUnit(), "Y"));
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
        siteRegisterRequest.setT3(siteCalculationT3(id, t2));

        t2.remove("MeanCR");
        t2.remove("MeanNCR");
        t2.remove("SAMPLESIZE");

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
            siteRegisterRequest.setT3(siteCalculationT3(id, t2));

            t2.remove("MeanCR");
            t2.remove("MeanNCR");
            t2.remove("SAMPLESIZE");

            siteRegisterRequest.setT2(t2);
            responseList.add(siteRegisterRequest);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public Map<String, Double> siteCalculationT1(Long id) throws Exception {
        Map<Long, Toxicity> chemicalData = chemicalController.getChemicalsData();
        List<SiteCalculation> siteContamiData = null;
        Double tw = 0d;
        Double ts = 0d;
        Map<String, Double> siteT1Vals = new HashMap<String, Double>();
        siteContamiData = siteCalculationRepository.findBySiteId(id);
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            Toxicity t = null;
            for (SiteCalculation siteCalc : siteContamiData) {
                t = chemicalData.get(siteCalc.getChemicalId());
                if ("Water".equalsIgnoreCase(siteCalc.getContaminationType())) {
                    tw += siteCalc.getContaminationValueInMilli() / Double.valueOf(t.getWaterGuideline());
                } else if ("Soil".equalsIgnoreCase(siteCalc.getContaminationType())) {
                    ts += siteCalc.getContaminationValueInMilli() / Double.valueOf(t.getSoilGuideline());
                }
            }
        }
        siteT1Vals.put("Water", tw);
        siteT1Vals.put("Soil", ts);

        return siteT1Vals;
    }

    public Map<String, Map<String, Double>> siteCalculationT2(Long id) throws Exception {
        Map<Long, Toxicity> chemicalData = chemicalController.getChemicalsData();
        Map<String, Consumption> consumptionData = consumptionController.getConsumptionAgeGrpData();
        List<SiteCalculation> siteContamiData = null;
        Map<String, Double> siteT2Vals = null;
        Map<String, Map<String, Double>> res = new HashMap<String, Map<String, Double>>();
        siteContamiData = siteCalculationRepository.findBySiteId(id);
        if (siteContamiData != null && !siteContamiData.isEmpty()) {
            for (String c : consumptionData.keySet()) {
                siteT2Vals = new HashMap<String, Double>();
                Toxicity t = null;
                Double val;
                Double ncr = 0d;
                Double cr = 0d;
                String valCRStr = "";
                String valNCRStr = "";
                Double bodyWt =0d;
                for (SiteCalculation siteCalc : siteContamiData) {
                    val = 0d;
                    bodyWt=Double.valueOf(consumptionData.get(c).getBodyWtAvg());
                    t = chemicalData.get(siteCalc.getChemicalId());
                    if ("Water".equalsIgnoreCase(siteCalc.getContaminationType())) {
                        val = (siteCalc.getContaminationValueInMilli() * Double.valueOf(consumptionData.get(c).getWaterConsAvg()))/bodyWt;
                        valNCRStr += val / Double.valueOf(t.getWaterRef()) + "~";
                        ncr += val / Double.valueOf(t.getWaterRef());
                    } else if ("Soil".equalsIgnoreCase(siteCalc.getContaminationType())) {
                        val = (siteCalc.getContaminationValueInMilli() * Double.valueOf(consumptionData.get(c).getSoilInvAvg()))/bodyWt;
                        valNCRStr += val / Double.valueOf(t.getSoilRef()) + "~";
                        ncr += val / Double.valueOf(t.getSoilRef());
                    } 
                    valCRStr += val * Double.valueOf(t.getCancerSlopeFactor()) + "~";
                    cr += val * Double.valueOf(t.getCancerSlopeFactor());
                }
                int contaSize = siteContamiData.size();
                siteT2Vals.put("NCR", ncr);
                siteT2Vals.put("CR", cr);
                siteT2Vals.put("MeanCR", cr / contaSize);
                siteT2Vals.put("MeanNCR", ncr / contaSize);
                siteT2Vals.put("NCR#" + valNCRStr, 0d);
                siteT2Vals.put("CR#" + valCRStr, 0d);
                siteT2Vals.put("SAMPLESIZE", Double.valueOf(contaSize));
                res.put(c, siteT2Vals);
            }
        }
        return res;
    }

    @GetMapping("/siteCalculations/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getSiteCalculations(@PathVariable("id") Long id) throws Exception {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("T1", siteCalculationT1(id));
        Map<String, Map<String, Double>> t2 = siteCalculationT2(id);
        resMap.put("T3", siteCalculationT3(id, t2));
        Map<String, Double> inMap = null;
        for (String s : t2.keySet()) {
            inMap = t2.get(s);
            Iterator itr = inMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry mapElement = (Map.Entry) itr.next();
                String key = (String) mapElement.getKey();
                if (key.contains("#")) {
                    itr.remove();
                }
            }
            inMap.remove("MeanCR");
            inMap.remove("MeanNCR");
            inMap.remove("SAMPLESIZE");
        }
        resMap.put("T2", t2);
        return resMap;
    }

    private Map<String, Map<String, Double>> siteCalculationT3(Long id, Map<String, Map<String, Double>> t2) {
        Map<String, Map<String, Double>> resMap = new HashMap<String, Map<String, Double>>();
        Map<String, Double> inMap = null;
        Map<String, Double> calcMap = null;
        for (String s : t2.keySet()) {
            inMap = t2.get(s);
            String tmpNCR = null;
            String tmpCR = null;
            Iterator itr = inMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry mapElement = (Map.Entry) itr.next();
                String key = (String) mapElement.getKey();
                if (key.contains("NCR#")) {
                    tmpNCR = key.substring(0, key.lastIndexOf("~")).replace("NCR#", "");
                } else if (key.contains("CR#")) {
                    tmpCR = key.substring(0, key.lastIndexOf("~")).replace("CR#", "");
                }
            }
            Double ncrMean = inMap.get("MeanNCR");
            Double crMean = inMap.get("MeanCR");
            Double sampleSize = inMap.get("SAMPLESIZE");
            String[] tmpNCRArr = tmpNCR.split("~");
            String[] tmpCRArr = tmpCR.split("~");

            Double ncrVar = calculateVariance(tmpNCRArr, ncrMean, sampleSize);
            Double ncrMU = calculateMU(ncrMean, ncrVar);
            Double ncrSigma = calculateSigma(ncrMean, ncrVar);
            LogNormalDistribution logNormalDistribution =null;
            
            /*LOGNRND with range-FOR TESTING*/
            Random rm=new Random(); rm.nextInt(5000);
            RandomGenerator rng=RandomGeneratorFactory.createRandomGenerator(rm);
            logNormalDistribution = new LogNormalDistribution(rng,ncrMU, ncrSigma, 1) ;          
            System.out.println("Random numbers::lognrnd::"+logNormalDistribution.sample()+"<<lognrndsamplesize>>"+logNormalDistribution.sample(5000));
            /*LOGNRND with range-FOR TESTING*/
           
            logNormalDistribution = new LogNormalDistribution(ncrMU, ncrSigma, 1);
            double randomValue = logNormalDistribution.sample();
            System.out.println("NCR>>ncrVar::" + ncrVar + "::ncrMU::" + ncrMU + "::ncrSigma::" + ncrSigma + "::ncrLogNrm::" + randomValue+ "::ncrLogNrm::" + logNormalDistribution.sample(5000));

            Double crVar = calculateVariance(tmpCRArr, crMean, sampleSize);
            Double crMU = calculateMU(crMean, crVar);
            Double crSigma = calculateSigma(crMean, crVar);
            logNormalDistribution = new LogNormalDistribution(crMU, crSigma, 1);
            double crRandomValue = logNormalDistribution.sample();
            System.out.println("CR>>crVar::" + crVar + "::crMU::" + crMU + "::crSigma::" + crSigma + "::crLogNrm::" + crRandomValue+ "::crLogNrm::" + logNormalDistribution.sample(5000));
            calcMap = new HashMap<String, Double>();
            calcMap.put("NCR", randomValue);
            calcMap.put("CR", crRandomValue);
            resMap.put(s, calcMap);
        }
        return resMap;
    }

    private Double calculateVariance(String[] indvArr, Double mean, Double sampleSize) {
        Double d = 0d;
        for (String s : indvArr) {
            d += (Math.pow((Double.valueOf(s) - mean), 2d));
        }
        return d / sampleSize;
    }

    private Double calculateMU(Double mean, Double variance) {
        //mu = log((m^2)/sqrt(v+m^2))
        Double meanSq = Math.pow(mean, 2);
        return Math.log(meanSq / (Math.sqrt(variance + meanSq)));
    }

    private Double calculateSigma(Double mean, Double variance) {
        //sigma = sqrt(log(v/(m^2)+1))
        Double meanSq = Math.pow(mean, 2);
        return Math.sqrt(Math.log(variance / (meanSq + 1)));
    }
}