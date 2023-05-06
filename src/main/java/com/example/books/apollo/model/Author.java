package com.example.books.apollo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class Author implements Serializable {

    public Author() {
    }

    public Author(String author) {
        this.name = author;
    }

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return Objects.equals(name.toUpperCase(Locale.ROOT), author.name.toUpperCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
