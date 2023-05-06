package com.example.books.rahvaraamat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RahvaRaamatRestClientTest {

    @InjectMocks
    private RahvaRaamatRestClient rahvaRaamatRestClient;

    @Test
    void shouldGetBooks() throws IOException, InterruptedException {
        var result = rahvaRaamatRestClient.getBooksByCategory(RahvaRaamatRestClient.PHOTOGRAPHY);
        System.out.println(result);
    }
}
