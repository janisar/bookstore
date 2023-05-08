package com.example.books.rahvaraamat.model;

import com.example.books.apollo.model.Author;
import com.example.books.model.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RahvaRamatBook extends Book {

    @JsonProperty("id")
    private Long id;

    @Override
    @JsonProperty("thumb_file_url")
    public String getImageUrl() {
        return super.getImageUrl();
    }

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
    public String getYear() {
        return super.getYear();
    }

    @JsonProperty("publisher")
    private void setPublisher(Map<String, Object> publisher) {
        if (publisher != null) {
            this.setPublisher((String) publisher.get("name"));
        }
    }

    @Override
    @JsonProperty("slug")
    public String getLink() {
        return super.getLink();
    }

    @JsonProperty("price")
    private void unpackNested(Map<String, Object> price) {
        this.setPrice(BigDecimal.valueOf(getPrice(price)));
    }

    @JsonProperty("categories")
    private void unpackTopics(List<LinkedHashMap<String, Object>> categories) {
        for (LinkedHashMap<String, Object> category : categories) {
            var categoryValue = (LinkedHashMap<String, Object>) category.get("category");
            var translations = (List<LinkedHashMap<String, Object>>) categoryValue.get("translations");
            for (LinkedHashMap<String, Object> translation : translations) {
                if (translation.get("language").equals("et")) {
                    this.getTopics().add((String) translation.get("name"));
                }
            }
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @JsonProperty("pages")
    public String getPages() {
        return super.getPages();
    }
}
