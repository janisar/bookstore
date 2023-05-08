package com.example.books.rahvaraamat;

import static com.example.books.TestUtils.getTestResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.books.rahvaraamat.model.RahvaRamatBook;
import com.example.books.rest.RestClient;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RahvaRaamatRestClientTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private RahvaRaamatRestClient rahvaRaamatRestClient;

    @Test
    void shouldGetBooks() throws IOException {
        RahvaRamatBook[] booksResponse = getTestResource("rahvaraamat-response.json", RahvaRamatBook[].class);
        when(restClient.get("https://web.rahvaraamat.ee/product/search?sort=-top&categoryLevel=2&categoryNavCode=GOLFREKET&page=1&productAvailabilityCodes[]=WEB&productType=BOOK", RahvaRamatBook[].class)).thenReturn(booksResponse);
        when(restClient.get(anyString(), eq(RahvaRamatBook.class))).thenReturn(null);
        var result = rahvaRaamatRestClient.getBooksByCategory("sport");

        assertEquals(20, result.size());
    }
}
