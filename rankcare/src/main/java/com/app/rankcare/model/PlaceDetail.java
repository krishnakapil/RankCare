package com.app.rankcare.model;

import java.util.List;

public class PlaceDetail {

    public List<PlaceResult> results;

    public static class PlaceResult {
        public String formatted_address;

        public GeoMetry geometry;

        public static class GeoMetry {

            public Location location;

            public static class Location {
                public Double lat;

                public Double lng;
            }
        }
    }

    public String getAddress() {
        if (results != null && results.size() > 0) {
            return results.get(0).formatted_address;
        }
        return null;
    }

    public Double getLat() {
        if (results != null && results.size() > 0) {
            return results.get(0).geometry.location.lat;
        }
        return 0.0;
    }

    public Double getLng() {
        if (results != null && results.size() > 0) {
            return results.get(0).geometry.location.lng;
        }
        return 0.0;
    }
}
