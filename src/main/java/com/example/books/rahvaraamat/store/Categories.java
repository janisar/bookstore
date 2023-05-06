package com.example.books.rahvaraamat.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Categories {

    public static HashMap<String, String> categories;

    static {
        Resource resource = new ClassPathResource("static/rahvaraamat-categories.json");
        try {
            String content = resource.getContentAsString(Charset.defaultCharset());
            categories = new ObjectMapper().readValue(content, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
