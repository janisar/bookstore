package com.example.books.apollo.model;

import com.example.books.model.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApolloBook extends Book {

    @Override
    @JsonProperty("authors")
    public List<Author> getAuthors() {
        return super.getAuthors();
    }

    @Override
    @JsonProperty("name")
    public String getTitle() {
        return super.getTitle();
    }

}
