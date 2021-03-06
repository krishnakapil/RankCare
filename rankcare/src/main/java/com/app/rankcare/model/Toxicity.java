package com.app.rankcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "toxicity_data")
public class Toxicity extends DateAudit {

    /**
     *
     */
    private static final long serialVersionUID = 3578212100596393592L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chemical_name", unique = true)
    private String chemicalName;

    @Column(name = "chemical_formula", unique = true)
    private String chemicalFormula;

    @Column(name = "soil_guideline")
    private String soilGuideline;

    @Column(name = "soil_ref")
    private String soilRef;

    @Column(name = "water_guideline")
    private String waterGuideline;

    @Column(name = "water_ref")
    private String waterRef;

    @Column(name = "cancer_slope_factor")
    private String cancerSlopeFactor;

    @Column(name = "cancer_slope_ref")
    private String cancerSlopeRef;

    @Column(name = "dosage_ref")
    private String dosageRefValue;

    @Column(name = "reference")
    private String reference;

    public Toxicity() {

    }

    public Toxicity(Long id, String chemicalName, String chemicalFormula, String soilGuideline, String soilRef,
                    String waterGuideline, String waterRef, String cancerSlopeFactor, String cancerSlopeRef,
                    String dosageRefValue, String reference) {
        this.id = id;
        this.chemicalName = chemicalName;
        this.chemicalFormula = chemicalFormula;
        this.soilGuideline = soilGuideline;
        this.soilRef = soilRef;
        this.waterGuideline = waterGuideline;
        this.waterRef = waterRef;
        this.cancerSlopeFactor = cancerSlopeFactor;
        this.cancerSlopeRef = cancerSlopeRef;
        this.dosageRefValue = dosageRefValue;
        this.reference = reference;
    }


    public Toxicity(String chemicalName, String chemicalFormula, String soilGuideline, String soilRef,
                    String waterGuideline, String waterRef, String cancerSlopeFactor, String cancerSlopeRef,
                    String dosageRefValue, String reference) {
        this.chemicalName = chemicalName;
        this.chemicalFormula = chemicalFormula;
        this.soilGuideline = soilGuideline;
        this.soilRef = soilRef;
        this.waterGuideline = waterGuideline;
        this.waterRef = waterRef;
        this.cancerSlopeFactor = cancerSlopeFactor;
        this.cancerSlopeRef = cancerSlopeRef;
        this.dosageRefValue = dosageRefValue;
        this.reference = reference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public String getSoilGuideline() {
        return soilGuideline;
    }

    public String getWaterGuideline() {
        return waterGuideline;
    }

    public String getCancerSlopeFactor() {
        return cancerSlopeFactor;
    }

    public String getDosageRefValue() {
        return dosageRefValue;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public void setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
    }

    public void setSoilGuideline(String soilGuideline) {
        this.soilGuideline = soilGuideline;
    }

    public String getSoilRef() {
        return soilRef;
    }

    public void setSoilRef(String soilRef) {
        this.soilRef = soilRef;
    }

    public void setWaterGuideline(String waterGuideline) {
        this.waterGuideline = waterGuideline;
    }

    public String getWaterRef() {
        return waterRef;
    }

    public void setWaterRef(String waterRef) {
        this.waterRef = waterRef;
    }

    public void setCancerSlopeFactor(String cancerSlopeFactor) {
        this.cancerSlopeFactor = cancerSlopeFactor;
    }

    public String getCancerSlopeRef() {
        return cancerSlopeRef;
    }

    public void setCancerSlopeRef(String cancerSlopeRef) {
        this.cancerSlopeRef = cancerSlopeRef;
    }

    public void setDosageRefValue(String dosageRefValue) {
        this.dosageRefValue = dosageRefValue;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Toxicity{" +
                "id=" + id +
                ", chemicalName='" + chemicalName + '\'' +
                ", chemicalFormula='" + chemicalFormula + '\'' +
                ", soilGuideline='" + soilGuideline + '\'' +
                ", soilRef='" + soilRef + '\'' +
                ", waterGuideline='" + waterGuideline + '\'' +
                ", waterRef='" + waterRef + '\'' +
                ", cancerSlopeFactor='" + cancerSlopeFactor + '\'' +
                ", cancerSlopeRef='" + cancerSlopeRef + '\'' +
                ", dosageRefValue='" + dosageRefValue + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
