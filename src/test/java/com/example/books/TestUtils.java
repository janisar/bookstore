package com.example.books;

import com.example.books.rahvaraamat.model.RahvaRamatBook;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

    public static <T> T getTestResource(String source, Class<T> tClass) throws IOException {
        var resource = new ClassPathResource(source).getContentAsString(Charset.defaultCharset());
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(resource, tClass);
    }
}
