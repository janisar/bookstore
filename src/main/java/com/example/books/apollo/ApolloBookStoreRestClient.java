package com.example.books.apollo;

import com.example.books.apollo.model.ApolloBook;
import com.example.books.apollo.model.CategoriesResponse;
import com.example.books.model.Book;
import com.example.books.model.BookStore;
import com.example.books.service.BookStoreRestClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApolloBookStoreRestClient implements BookStoreRestClient {

    public static final String PHOTOGRAPHY = "FOTOGRAAFI";

    private final Map<String, Integer> categories;

    public ApolloBookStoreRestClient() {
        Map<String, Integer> categories;
        try {
            categories = this.loadCategories();

        } catch (InterruptedException e) {
            e.printStackTrace();
            categories = null;
        }
        this.categories = categories;
    }

    @Override
    public List<Book> getBooksByCategory(String category) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                        URI.create("https://apl-api.apollo.ee/api/catalog/apollo_magento_catalog_et/product/_search"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(getCategoryInt(category), Charset.defaultCharset()))
                .build();

        var responseString = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .readValue(responseString.body(), Book[].class);

        return Arrays.stream(result).toList();
    }

    private String getCategoryInt(String category) {
        return "{\n"
                + "    \"query\": {\n"
                + "        \"bool\": {\n"
                + "            \"filter\": {\n"
                + "                \"bool\": {\n"
                + "                    \"must\": [\n"
                + "                        {\n"
                + "                            \"terms\": {\n"
                + "                                \"visibility\": [\n"
                + "                                    2,\n"
                + "                                    3,\n"
                + "                                    4\n"
                + "                                ]\n"
                + "                            }\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"terms\": {\n"
                + "                                \"status\": [\n"
                + "                                    0,\n"
                + "                                    1\n"
                + "                                ]\n"
                + "                            }\n"
                + "                        },\n"
                + "                        {\n"
                + "                            \"terms\": {\n"
                + "                                \"category_ids\": [\n"
                + this.categories.get(category)
                + "\n"
                + "                                ]\n"
                + "                            }\n"
                + "                        }\n"
                + "                    ]\n"
                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "}";
    }

    @Override
    public boolean categoryExists(String category) {
        return categories.containsKey(category);
    }

    @Override
    public List<String> getAllCategories() {
        return categories.keySet().stream().toList();
    }

    private Map<String, Integer> loadCategories() throws InterruptedException {
        var client = HttpClient.newHttpClient();

        var url = "https://apl-api.apollo.ee/api/catalog/apollo_magento_catalog_et/category/_search";
        Resource resource = new ClassPathResource("static/apollo-categories-request.json");
        try {
            Map<String, Integer> categories = new HashMap<>();
            String content = resource.getContentAsString(Charset.defaultCharset());
            var request = HttpRequest.newBuilder(URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(content))
                    .build();

            var responseString = client.send(request, HttpResponse.BodyHandlers.ofString());

            var result = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                    .readValue(responseString.body(), CategoriesResponse.class);

            ArrayList hits = (ArrayList) result.getHits().get("hits");
            LinkedHashMap<String, Object> source =
                    (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) hits.get(0)).get("_source");
            ArrayList<LinkedHashMap<String, Object>> data =
                    (ArrayList<LinkedHashMap<String, Object>>) source.get("children_data");

            populateCategories(categories, data);
            return categories;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    private void populateCategories(Map<String, Integer> categories, ArrayList<LinkedHashMap<String, Object>> data) {
        data.forEach(d -> {
            if (d.get("children_data") == null) {
                categories.put((String) d.get("name"), (Integer) d.get("id"));
            } else {
                populateCategories(categories, (ArrayList<LinkedHashMap<String, Object>>) d.get("children_data"));
            }
        });
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
