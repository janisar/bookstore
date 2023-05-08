package com.example.books.apollo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ApolloBook implements Serializable {

    private String name;
    @JsonProperty("url_path")
    private String link;
    @JsonProperty("lk")
    private String pages;
    @JsonProperty("kirjastus")
    private String publisher;
    @JsonProperty("autor")
    private String author;
    @JsonProperty("ilmus")
    private String year;
    @JsonProperty("price")
    private double price;
    @JsonProperty("image")
    private String imagePath;

    private List<String> topics = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @JsonProperty("category")
    private void unpackTopics(List<LinkedHashMap<String, Object>> categories) {
        for (LinkedHashMap<String, Object> category : categories) {
            topics.add((String) category.get("name"));
        }
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
