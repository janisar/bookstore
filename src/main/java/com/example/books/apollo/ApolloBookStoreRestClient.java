package com.example.books.apollo;

import com.example.books.apollo.model.CategoriesResponse;
import com.example.books.model.BookStore;
import com.example.books.service.BookStoreRestClient;
import com.example.books.apollo.model.ApolloBook;
import com.example.books.model.Book;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ApolloBookStoreRestClient implements BookStoreRestClient {

    public static final String PHOTOGRAPHY = "FOTOGRAAFI";

    @Override
    public List<Book> getBooksByCategory(String category) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                        URI.create(
                                "https://web.rahvaraamat.ee/product/search?categoryLevel=2&page=1&productAvailabilityCodes[]=WEB&productType=BOOK&categoryNavCode="
                                        + category))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .build();

        var responseString = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .readValue(responseString.body(), Book[].class);

        return Arrays.stream(result).toList();
    }

    public List<String> getCategories() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var url = "https://apl-api.apollo.ee/api/catalog/apollo_magento_catalog_et/category/_search";
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"query\":{\"match_all\":{}}}"))
                .build();

        var responseString = client.send(request, HttpResponse.BodyHandlers.ofString());

        var result = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .readValue(responseString.body(), CategoriesResponse.class);
        return null;
    }

    private Book mapToBook(ApolloBook apolloBook) {
        var book = new Book();
        book.setAuthors(apolloBook.getAuthors());
        book.setTitle(apolloBook.getTitle());
        book.setBookStore(BookStore.APOLLO);
        book.setPublisher(apolloBook.getPublisher());
        book.setPrice(apolloBook.getPrice());
        book.setYear(apolloBook.getYear());
        return book;
    }
}
