package com.app.rankcare.model;

import java.util.List;

public class AutoCompleteResponse {
    public List<Place> predictions;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AutoComplete Response : ").append("\n\n");

        if (predictions != null) {
            for (Place place : predictions) {
                sb.append(place);
            }
        }

        return sb.toString();
    }
}
