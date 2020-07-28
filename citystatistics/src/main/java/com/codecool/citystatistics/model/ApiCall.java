package com.codecool.citystatistics.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ApiCall {

    private RemoteURLReader remoteURLReader;
    public ApiCall(RemoteURLReader remoteURLReader) {
        this.remoteURLReader = remoteURLReader;
    }

    public JSONObject getResult(String apiPath) throws IOException, JSONException {
        String result = remoteURLReader.readFromUrl(apiPath);
        return new JSONObject(result);
    }
}
