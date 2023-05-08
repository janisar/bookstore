package com.example.books.model;

import com.example.books.apollo.model.Author;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Book implements Serializable {

    private List<Author> authors;
    private String title;
    private BigDecimal price;
    private String language;
    private List<String> topics;
    private String publisher;
    private String link; //???
    private String year;
    private String pages;
    private String imageUrl;

    private BookStore bookStore;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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
        if (topics == null) {
            topics = new java.util.ArrayList<>();
        }
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
}
