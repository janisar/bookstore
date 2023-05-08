package com.example.books.rahvaraamat;

import com.example.books.model.Book;
import com.example.books.model.BookStore;
import com.example.books.rahvaraamat.model.RahvaRamatBook;
import com.example.books.rest.RestClient;
import com.example.books.service.BookStoreRestClient;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RahvaRaamatRestClient implements BookStoreRestClient {

    private static final String PUBLIC_URL = "https://rahvaraamat.ee/p/";
    private static final String DEFAULT_IMAGE = "https://rahvaraamat.ee/_next/image?url=%2F_next%2Fstatic%2Fimage%2Fpublic%2Ficons%2Fra-icon.683324c10927790e0c04b1dce1a5a2be.svg&w=256&q=75";

    @Autowired
    private RestClient restClient;

    @Override
    public List<Book> getBooksByCategory(String topic) {
        var category = getCategory(topic);
        var response = restClient.get(getUri(category), RahvaRamatBook[].class);

        return Arrays.stream(response).parallel().map(book -> this.mapToBook(book, category)).toList();
    }


    @Override
    public boolean categoryExists(String category) {
        return !getCategory(category).isEmpty();
    }

    @Override
    public List<String> getAllCategories() {
        return Categories.categoriesMap.values().stream().toList();
    }

    private String getCategory(String category) {
        var categories = Categories.categoriesMap.entrySet().stream()
                .filter(c -> c.getValue().equalsIgnoreCase(category)
                        || c.getValue().toUpperCase(Locale.ROOT).contains(category.toUpperCase(Locale.ROOT)))
                .map(Map.Entry::getKey).toList();
        return categories.isEmpty() ? "" : categories.get(0);
    }

    private Book mapToBook(RahvaRamatBook rahvaRamatBook, String category) {
        var book = new Book();
        var rrBook = restClient.get(getBookUrl(rahvaRamatBook.getId()), RahvaRamatBook.class);
        if (rrBook != null) {
            book.setPages(rrBook.getPages());
            book.setTopics(rrBook.getTopics());
        }

        book.setAuthors(rahvaRamatBook.getAuthors());
        book.setTitle(rahvaRamatBook.getTitle());
        book.setBookStore(BookStore.RAHVA_RAAMAT);
        book.setPublisher(rahvaRamatBook.getPublisher());
        book.setPrice(rahvaRamatBook.getPrice());
        book.setYear(rahvaRamatBook.getYear());
        book.setTopics(rahvaRamatBook.getTopics());
        book.setLanguage(rahvaRamatBook.getLanguage());
        book.getTopics().add(category);
        book.setLink(PUBLIC_URL + rahvaRamatBook.getLink() + "/" + rahvaRamatBook.getId() + "/et");
        book.setImageUrl(rahvaRamatBook.getImageUrl() != null ? rahvaRamatBook.getImageUrl() : DEFAULT_IMAGE);
        return book;
    }


    private String getBookUrl(Long id) {
        return "https://web.rahvaraamat.ee/product/view?id=" + id;
    }

    private String getUri(String category) {
        return "https://web.rahvaraamat.ee/product/search?sort=-top&categoryLevel=2&categoryNavCode="
                + category + "&page=1&productAvailabilityCodes[]=WEB&productType=BOOK";
    }
}
