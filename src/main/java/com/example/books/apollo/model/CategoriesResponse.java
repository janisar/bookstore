package com.example.books.apollo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CategoriesResponse {

    @JsonProperty("hits")
    private Map<String, Object> hits;

    public Map<String, Object> getHits() {
        return hits;
    }

    public void setHits(Map<String, Object> hits) {
        this.hits = hits;
    }
}
