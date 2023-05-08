package com.example.books.apollo.model;

import java.io.Serializable;
import java.util.List;

public class Hits implements Serializable {
    
    private int total;
    private int max_score;
    private List<Hit> hits;

    public Hits() {}
    public int getMax_score() {
        return max_score;
    }

    public void setHits(
            List<Hit> hits) {
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
