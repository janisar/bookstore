package com.example.books.apollo.model;

import java.io.Serializable;

public class SearchResponse implements Serializable {

    private Hits hits;

    public SearchResponse() {}

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }
}
