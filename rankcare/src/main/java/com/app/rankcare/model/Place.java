package com.app.rankcare.model;

public class Place {
    public String description;

    public String place_id;

    @Override
    public String toString() {
        return "Place{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }
}
