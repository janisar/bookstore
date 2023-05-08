package com.example.books.apollo;

import com.example.books.apollo.model.ApolloBook;
import com.example.books.apollo.model.Author;
import com.example.books.apollo.model.CategoriesResponse;
import com.example.books.apollo.model.SearchResponse;
import com.example.books.model.Book;
import com.example.books.model.BookStore;
import com.example.books.model.BookStoreException;
import com.example.books.rest.RestClient;
import com.example.books.service.BookStoreRestClient;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApolloBookStoreRestClient implements BookStoreRestClient {

    private static final String APOLLO_PUBLIC_URL = "https://www.apollo.ee/";
    private static final String IMAGE_URL = "https://apl-api.apollo.ee/img/600/744/resize/catalog/product/";

    private final Logger logger = Logger.getLogger(ApolloBookStoreRestClient.class.getName());

    private static final String ES_PRODUCTS_HOST =
            "https://apl-api.apollo.ee/api/catalog/apollo_magento_catalog_et/product/_search";

    private static final String ES_CATEGORIES_HOST =
            "https://apl-api.apollo.ee/api/catalog/apollo_magento_catalog_et/category/_search";

    private final Map<String, Integer> categories;

    private final RestClient restClient;

    public ApolloBookStoreRestClient(@Autowired RestClient restClient) {
        this.restClient = restClient;
        this.categories = this.loadCategories();
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        var requestBody = getBooksQuery(category);
        var result = restClient.post(ES_PRODUCTS_HOST, requestBody, SearchResponse.class);
        if (result.getHits() == null || result.getHits().getHits() == null) {
            return new ArrayList<>();
        }
        return result.getHits().getHits().stream().map(hit -> this.mapToBook(hit.get_source(), category)).toList();
    }

    @Override
    public boolean categoryExists(String category) {
        return !getCategories(category).isEmpty();
    }

    @Override
    public List<String> getAllCategories() {
        return categories.keySet().stream().toList();
    }

    private String getBooksQuery(String category) {
        Resource resource = new ClassPathResource("static/apollo-books-request.json");
        try {
            String content = resource.getContentAsString(Charset.defaultCharset());
            return content.replace("{{categories}}", getCategories(category));
        } catch (IOException e) {
            logger.warning("Failed to read file: " + resource.getFilename());
            throw new BookStoreException("Failed to read file: " + resource.getFilename());
        }
    }

    private CharSequence getCategories(String category) {
        return this.categories.keySet().stream()
                .filter(c -> c.equalsIgnoreCase(category)
                        || c.toUpperCase(Locale.ROOT).contains(category.toUpperCase(Locale.ROOT)))
                .map(this.categories::get)
                .map(Object::toString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> loadCategories() {
        Resource resource = new ClassPathResource("static/apollo-categories-request.json");
        Map<String, Integer> categories = new HashMap<>();
        String content = getCategoriesRequestBody(resource);
        var result = restClient.post(ES_CATEGORIES_HOST, content, CategoriesResponse.class);

        ArrayList<LinkedHashMap<String, Object>> hits =
                (ArrayList<LinkedHashMap<String, Object>>) result.getHits().get("hits");
        LinkedHashMap<String, Object> source =
                (LinkedHashMap<String, Object>) hits.get(0).get("_source");
        ArrayList<LinkedHashMap<String, Object>> data =
                (ArrayList<LinkedHashMap<String, Object>>) source.get("children_data");

        populateCategories(categories, data);
        return categories;
    }

    private String getCategoriesRequestBody(Resource resource) {
        try {
            return resource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            logger.warning("Failed to load categories from Apollo");
            throw new BookStoreException("Failed to load categories from Apollo");
        }
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

    private Book mapToBook(ApolloBook apolloBook, String category) {
        var book = new Book();
        book.setAuthors(List.of(new Author(apolloBook.getAuthor())));
        book.setTitle(apolloBook.getName());
        book.setBookStore(BookStore.APOLLO);
        book.setPages(apolloBook.getPages());
        book.setPublisher(apolloBook.getPublisher());
        book.setPrice(BigDecimal.valueOf(apolloBook.getPrice()));
        book.setYear(apolloBook.getYear());
        book.getTopics().add(category);
        book.setLink(APOLLO_PUBLIC_URL + apolloBook.getLink());
        book.setImageUrl(IMAGE_URL + apolloBook.getImagePath());
        return book;
    }
}
