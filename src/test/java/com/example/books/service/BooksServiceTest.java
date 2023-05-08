package com.example.books.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.books.apollo.ApolloBookStoreRestClient;
import com.example.books.apollo.model.Author;
import com.example.books.model.Book;
import com.example.books.model.BookStoreException;
import com.example.books.rahvaraamat.RahvaRaamatRestClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private ApolloBookStoreRestClient apolloBookStoreRestClient;
    @Mock
    private RahvaRaamatRestClient rahvaRaamatRestClient;

    private BookService bookService;

    @BeforeEach
    void setup() {
        this.bookService = new BookService(apolloBookStoreRestClient, rahvaRaamatRestClient);
    }

    @Test
    void shouldGetBooksIfCategoryExists() {
        var aBook = new Book();
        aBook.setTitle("Apollo book");
        aBook.setAuthors(List.of(new Author("John Doe")));
        var rrBook = new Book();
        rrBook.setAuthors(List.of(new Author("Jane Doe")));
        rrBook.setTitle("Rahva Raamat book");
        when(apolloBookStoreRestClient.categoryExists("sport")).thenReturn(true);
        when(apolloBookStoreRestClient.getBooksByCategory("sport")).thenReturn(List.of(aBook));
        when(rahvaRaamatRestClient.getBooksByCategory("sport")).thenReturn(List.of(rrBook));
        var books = bookService.getBooksByCategory("sport");

        assertEquals(2, books.size());
    }

    @Test
    void shouldNotGetBooksIfCategoryNotExists() {
        when(apolloBookStoreRestClient.categoryExists("sport")).thenReturn(false);
        when(rahvaRaamatRestClient.categoryExists("sport")).thenReturn(false);
        assertThrows(BookStoreException.class, () -> bookService.getBooksByCategory("sport"));
    }

    @Test
    void shouldGetBooksFromCache() {
        var aBook = new Book();
        aBook.setTitle("Apollo book");
        aBook.setAuthors(List.of(new Author("John Doe")));
        var rrBook = new Book();
        rrBook.setAuthors(List.of(new Author("Jane Doe")));
        rrBook.setTitle("Rahva Raamat book");
        when(apolloBookStoreRestClient.categoryExists("sport")).thenReturn(true);
        when(apolloBookStoreRestClient.getBooksByCategory("sport")).thenReturn(List.of(aBook));
        when(rahvaRaamatRestClient.getBooksByCategory("sport")).thenReturn(List.of(rrBook));

        bookService.getBooksByCategory("sport");

        var cachedBooks = bookService.getBooksByCategory("sport");

        verify(apolloBookStoreRestClient, times(1)).getBooksByCategory("sport");
        verify(rahvaRaamatRestClient, times(1)).getBooksByCategory("sport");

        assertEquals(2, cachedBooks.size());
    }

    @Test
    void shouldGetBooksByAuthor() {
        var aBook = new Book();
        aBook.setTitle("Apollo book");
        aBook.setAuthors(List.of(new Author("John Doe")));
        var aBook2 = new Book();
        aBook2.setTitle("Apollo book 2");
        aBook2.setAuthors(List.of(new Author("John Doe")));
        var rrBook = new Book();
        rrBook.setAuthors(List.of(new Author("Jane Doe")));
        rrBook.setTitle("Rahva Raamat book");
        when(apolloBookStoreRestClient.categoryExists("sport")).thenReturn(true);
        when(apolloBookStoreRestClient.getBooksByCategory("sport")).thenReturn(List.of(aBook, aBook2));
        when(rahvaRaamatRestClient.getBooksByCategory("sport")).thenReturn(List.of(rrBook));

        bookService.getBooksByCategory("sport");

        var authorBooks = bookService.getAllBooks("John Doe");

        assertEquals(2, authorBooks.size());
    }

    @Test
    void shouldGetAllBooks() {
        var aBook = new Book();
        aBook.setTitle("Apollo book");
        aBook.setAuthors(List.of(new Author("John Doe")));
        var rrBook = new Book();
        rrBook.setAuthors(List.of(new Author("Jane Doe")));
        rrBook.setTitle("Rahva Raamat book");
        when(apolloBookStoreRestClient.categoryExists("sport")).thenReturn(true);
        when(apolloBookStoreRestClient.getBooksByCategory("sport")).thenReturn(List.of(aBook));
        when(rahvaRaamatRestClient.getBooksByCategory("sport")).thenReturn(List.of(rrBook));

        bookService.getBooksByCategory("sport");

        var authorBooks = bookService.getAllBooks(null);

        assertEquals(2, authorBooks.size());
    }

    @Test
    void shouldGetAllCategories() {
        when(apolloBookStoreRestClient.getAllCategories()).thenReturn(List.of("1", "2"));
        when(rahvaRaamatRestClient.getAllCategories()).thenReturn(List.of("2", "3"));

        var categories = bookService.getAllCategories();

        assertEquals(4, categories.size());
    }
}
