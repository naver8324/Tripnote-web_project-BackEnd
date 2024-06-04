package com.elice.tripnote.domain.spot.naver.dto;

import java.util.List;

public class GeocodeRes {
    private List<CoordinateResult> results;

    public List<CoordinateResult> getResults() {
        return results;
    }

    public void setResults(List<CoordinateResult> results) {
        this.results = results;
    }
}
