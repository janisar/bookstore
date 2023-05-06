package com.example.books.service;

import com.example.books.apollo.ApolloBookStoreRestClient;
import com.example.books.apollo.model.Author;
import com.example.books.model.Book;
import com.example.books.rahvaraamat.RahvaRaamatRestClient;
import com.example.books.rahvaraamat.store.Categories;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final List<BookStoreRestClient> bookStoreRestClients = new ArrayList<>();

    private final Map<String, List<Book>> bookCache = new HashMap<>();

    private final Map<Author, List<Book>> authorCache = new HashMap<>();

    public BookService(@Autowired ApolloBookStoreRestClient apolloBookStoreRestClient,
                       @Autowired RahvaRaamatRestClient rahvaRaamatRestClient) {
        bookStoreRestClients.add(apolloBookStoreRestClient);
        bookStoreRestClients.add(rahvaRaamatRestClient);
    }

    public List<Book> getBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();

        if (bookCache.containsKey(category)) {
            return bookCache.get(category);
        }
        getBooksFromBookStores(category, books);
        return books;
    }

    private void getBooksFromBookStores(String category, List<Book> books) {
        if (bookStoreRestClients.stream().noneMatch(client -> client.categoryExists(category))){
            throw new IllegalArgumentException("Category does not exist");
        }

        bookStoreRestClients.forEach(restClient -> {
            try {
                List<Book> booksFromStore = restClient.getBooksByCategory(category);
                books.addAll(booksFromStore);
                populateAuthors(booksFromStore);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to get books from " + restClient.getClass().getSimpleName());
            }
        });

        bookCache.putIfAbsent(category, books);
    }

    private void populateAuthors(List<Book> booksFromStore) {
        booksFromStore.forEach(book -> book.getAuthors().forEach(author -> {
            if (authorCache.containsKey(author)) {
                authorCache.get(author).add(book);
            } else {
                List<Book> books = new ArrayList<>();
                books.add(book);
                authorCache.put(author, books);
            }
        }));
    }

    public List<Book> getAllBooks(String author) {
        if (author != null) {
            return authorCache.get(new Author(author));
        }
        return bookCache.values().stream().flatMap(List::stream).toList();
    }

    public List<String> getAllCategories() {
        var result = new ArrayList<String>();
        bookStoreRestClients.forEach(restClient -> result.addAll(restClient.getAllCategories()));
        result.sort(String::compareTo);
        return result;
    }
}
