package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.model.ApiCall;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CityController {

    @Autowired
    ApiCall cityStatisticsResource;

    @GetMapping("/")
    public void result() throws IOException, JSONException {
        JSONObject result = cityStatisticsResource.getResult();
        System.out.println("This is " + result.get("name"));
    }
}
