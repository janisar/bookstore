package com.example.books.apollo.model;

import java.io.Serializable;

public class Hit implements Serializable {
    private ApolloBook _source;

    public Hit() {}
    public ApolloBook get_source() {
        return _source;
    }

    public void set_source(ApolloBook _source) {
        this._source = _source;
    }
}
