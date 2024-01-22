package com.tortora.financas.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class HttpUtils {

    HttpUtils() {
    }

    public HttpURLConnection get(String url) {
        try {
            HttpURLConnection conn = this.openConnection(url);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            return conn;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public StringBuilder reader(HttpURLConnection conn) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();

            return output;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public HttpURLConnection openConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

}
