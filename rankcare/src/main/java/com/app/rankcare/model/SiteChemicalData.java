package com.app.rankcare.model;

public class SiteChemicalData {
    private double soilMean;

    private double soilSd;

    private double waterMean;

    private double waterSd;

    private Toxicity toxicity;

    public SiteChemicalData() {
    }

    public double getSoilMean() {
        return soilMean;
    }

    public double getSoilSd() {
        return soilSd;
    }

    public double getWaterMean() {
        return waterMean;
    }

    public double getWaterSd() {
        return waterSd;
    }

    public Toxicity getToxicity() {
        return toxicity;
    }

    public void setSoilMean(double soilMean) {
        this.soilMean = soilMean;
    }

    public void setSoilSd(double soilSd) {
        this.soilSd = soilSd;
    }

    public void setWaterMean(double waterMean) {
        this.waterMean = waterMean;
    }

    public void setWaterSd(double waterSd) {
        this.waterSd = waterSd;
    }

    public void setToxicity(Toxicity toxicity) {
        this.toxicity = toxicity;
    }
}
