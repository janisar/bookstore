package com.example.books.service;

import com.example.books.model.Book;
import java.io.IOException;
import java.util.List;

public interface BookStoreRestClient {

    List<Book> getBooksByCategory(String category) throws IOException, InterruptedException;
}
