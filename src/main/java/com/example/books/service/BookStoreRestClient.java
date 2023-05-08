package com.example.books.service;

import com.example.books.model.Book;
import java.util.List;

public interface BookStoreRestClient {

    List<Book> getBooksByCategory(String category);

    boolean categoryExists(String category);

    List<String> getAllCategories();
}
