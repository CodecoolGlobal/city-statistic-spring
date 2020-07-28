package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.instance_creator.CityScoresCreator;
import com.codecool.citystatistics.model.ApiCall;
import com.codecool.citystatistics.model.CityScores;
import com.codecool.citystatistics.model.Score;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    ApiCall apiCall;

    @Autowired
    CityScoresCreator cityScores;

    @GetMapping("/")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CityScores> result() throws IOException, JSONException {
        ArrayList<CityScores> listOfCitiesByScores = new ArrayList<>();
        List<String> defaultCities = Arrays.asList("budapest", "barcelona", "melbourne");
        for (String defaultCity : defaultCities){
            ArrayList<Score> scoreArrayList = new ArrayList<>();
            CityScores cityResult = cityScores.createCityScores();
            JSONObject result = apiCall.getResult("https://api.teleport.org/api/urban_areas/slug:"+ defaultCity +"/scores/");

            JSONArray categories = (JSONArray) result.get("categories");
            for (int i = 0; i < categories.length(); i++) {
                scoreArrayList.add(new Score(categories.getJSONObject(i).getString("name"),categories.getJSONObject(i).getInt("score_out_of_10")));
            }

            cityResult.setCityName(defaultCity);
            cityResult.setScores(scoreArrayList);
            listOfCitiesByScores.add(cityResult);
        }

        return listOfCitiesByScores;
    }
}
