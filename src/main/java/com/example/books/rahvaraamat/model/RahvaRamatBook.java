package com.example.books.rahvaraamat.model;

import com.example.books.apollo.model.Author;
import com.example.books.model.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RahvaRamatBook extends Book {

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

    @Override
    @JsonProperty("year")
    public short getYear() {
        return super.getYear();
    }

    @Override
    @JsonProperty("slug")
    public String getLink() {
        return super.getLink();
    }
}
