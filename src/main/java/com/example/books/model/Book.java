package com.example.books.model;

import com.example.books.apollo.model.Author;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book implements Serializable {

    @JsonProperty("authors")
    private List<Author> authors;
    @JsonProperty("name")
    private String title;
    private BigDecimal price;
    private String language;
    private List<String> topics;
    private String publisher;
    private String link; //???
    @JsonProperty("year")
    private short year;
    private short pages;

    private BookStore bookStore;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookStore getBookStore() {
        return bookStore;
    }

    public void setBookStore(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @JsonProperty("price")
    private void unpackNested(Map<String, Object> price) {
        this.setPrice(BigDecimal.valueOf(getPrice(price)));
    }

    @JsonProperty("languageType")
    private void unpackLanguage(Map<String, Object> language) {
        if (language != null) {
            this.setLanguage((String) language.get("name"));
        }
    }

    private Double getPrice(Map<String, Object> price) {
        var amount = price.get("price");
        if (amount instanceof Double) {
            return (Double) amount;
        } else if (amount instanceof Integer) {
            return ((Integer) amount).doubleValue();
        } else {
            throw new IllegalArgumentException("Unknown price type: " + amount.getClass());
        }
    }

    @JsonProperty("publisher")
    private void setPublisher(Map<String, Object> publisher) {
        if (publisher != null) {
            this.setPublisher((String) publisher.get("name"));
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("year")
    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getPages() {
        return pages;
    }

    public void setPages(short pages) {
        this.pages = pages;
    }
}
