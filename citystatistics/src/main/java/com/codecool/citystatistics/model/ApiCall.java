package com.codecool.citystatistics.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ApiCall {

    private RemoteURLReader remoteURLReader;
    private static final String apiPath = "https://api.teleport.org/api/cities/geonameid:5391959/";

    public ApiCall(RemoteURLReader remoteURLReader) {
        this.remoteURLReader = remoteURLReader;
    }

    public JSONObject getResult() throws IOException, JSONException {
        String result = remoteURLReader.readFromUrl(apiPath);
        JSONObject json = new JSONObject(result);
        String name = json.get("name").toString();
        String pop = json.get("population").toString();
        return json;
    }
}
