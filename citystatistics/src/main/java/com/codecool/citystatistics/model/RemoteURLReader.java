package com.codecool.citystatistics.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class RemoteURLReader {

    public String readFromUrl(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String lines = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        return lines;
    }
}
