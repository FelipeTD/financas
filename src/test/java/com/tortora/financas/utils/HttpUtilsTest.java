package com.tortora.financas.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HttpUtilsTest {

    @Autowired
    private HttpUtils httpUtils;

    @Test
    void getTest() throws IOException {
        HttpURLConnection response = httpUtils.get("https://google.com.br");
        Assertions.assertEquals(200, response.getResponseCode());
        response.disconnect();
    }

    @Test
    void getRuntimeExceptionTest() {
        Assertions.assertThrows(RuntimeException.class, () -> httpUtils.get(null));
    }

    @Test
    void readerTest() {
        HttpURLConnection response = httpUtils.get("https://google.com.br");
        StringBuilder output = httpUtils.reader(response);
        Assertions.assertNotNull(output);
    }

    @Test
    void readerExceptionTest() throws IOException {
        HttpURLConnection response = mock(HttpURLConnection.class);
        when(response.getInputStream()).thenThrow(new IOException());
        Assertions.assertThrows(RuntimeException.class, () -> httpUtils.reader(response));
    }

}
