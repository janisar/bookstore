package com.example.books.apollo;

import static com.example.books.TestUtils.getTestResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.books.apollo.model.CategoriesResponse;
import com.example.books.apollo.model.SearchResponse;
import com.example.books.rest.RestClient;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApolloBookStoreRestClientTest {

    @Mock
    private RestClient restClient;

    private ApolloBookStoreRestClient apolloBookStoreRestClient;

    @BeforeEach
    void setUp() throws IOException {
        CategoriesResponse categoriesResponse =
                getTestResource("apollo-categories.json", CategoriesResponse.class);
        when(restClient.post(anyString(), anyString(), eq(CategoriesResponse.class))).thenReturn(categoriesResponse);

        this.apolloBookStoreRestClient = new ApolloBookStoreRestClient(restClient);
    }

    @Test
    void shouldGetBooks() throws IOException {
        SearchResponse booksResponse = getTestResource("apollo-response.json", SearchResponse.class);
        when(restClient.post(anyString(), anyString(), eq(SearchResponse.class))).thenReturn(booksResponse);
        var result = apolloBookStoreRestClient.getBooksByCategory("1139");

        assertEquals(10, result.size());
    }
}
