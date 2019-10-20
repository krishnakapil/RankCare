package com.app.rankcare.payload;

public class ChemicalsResponse {
    private Long id;

    private String chemicalName;

    public ChemicalsResponse(Long id, String chemicalName) {
        this.id = id;
        this.chemicalName = chemicalName;
    }

    public Long getId() {
        return id;
    }

    public String getChemicalName() {
        return chemicalName;
    }
}
