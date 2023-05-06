package com.example.books.rahvaraamat;

import com.example.books.model.Book;
import com.example.books.model.BookStore;
import com.example.books.rahvaraamat.model.RahvaRamatBook;
import com.example.books.service.BookStoreRestClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RahvaRaamatRestClient implements BookStoreRestClient {

    public static final String TRANSPORT = "TRANSPORT";
    public static final String PHOTOGRAPHY = "FOTOGRAAFI";

    @Override
    public List<Book> getBooksByCategory(String category) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                        URI.create(
                                "https://web.rahvaraamat.ee/product/search?sort=-top&categoryLevel=2&categoryNavCode="
                                        + category + "&page=1&productAvailabilityCodes[]=WEB&productType=BOOK"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .build();

        var responseString = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .readValue(responseString.body(), Book[].class);
        return Arrays.stream(result).toList();
    }

    private Book mapToBook(RahvaRamatBook rahvaRamatBook) {
        var book = new Book();
        book.setAuthors(rahvaRamatBook.getAuthors());
        book.setTitle(rahvaRamatBook.getTitle());
        book.setBookStore(BookStore.RAHVA_RAAMAT);
        book.setPublisher(rahvaRamatBook.getPublisher());
        book.setPrice(rahvaRamatBook.getPrice());
        book.setYear(rahvaRamatBook.getYear());
        return book;
    }
}
