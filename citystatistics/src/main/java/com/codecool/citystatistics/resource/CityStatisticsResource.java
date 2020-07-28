package com.codecool.citystatistics.resource;

import com.codecool.citystatistics.model.RemoteURLReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CityStatisticsResource {

    private RemoteURLReader remoteURLReader;
    private static final String apiPath = "https://api.teleport.org/api/cities/geonameid:5391959/";

    public CityStatisticsResource(RemoteURLReader remoteURLReader) {
        this.remoteURLReader = remoteURLReader;
    }

    public JSONObject getResult() throws IOException, JSONException {
        String result = remoteURLReader.readFromUrl(apiPath);
        JSONObject json = new JSONObject(result);
        String name = json.get("name").toString();
        String pop = json.get("population").toString();
//        System.out.println("Running...");
//        System.out.println(name);
//        System.out.println(pop);
//        System.out.println(json);
        return json;
    }
}
