package com.example.books.apollo;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApolloBookStoreRestClientTest {

    @InjectMocks
    private ApolloBookStoreRestClient apolloBookStoreRestClient;

    @Test
    void shouldGetBooks() throws IOException, InterruptedException {
        apolloBookStoreRestClient.getBooksByCategory(ApolloBookStoreRestClient.PHOTOGRAPHY);
    }
}
