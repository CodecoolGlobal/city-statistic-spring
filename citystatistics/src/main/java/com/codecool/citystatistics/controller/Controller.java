package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.instance_creator.CityScoresCreator;
import com.codecool.citystatistics.instance_creator.CitySmallCardCreator;
import com.codecool.citystatistics.model.ApiCall;
import com.codecool.citystatistics.model.CityScores;
import com.codecool.citystatistics.model.CitySmallCard;
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

    @Autowired
    CitySmallCardCreator citySmallCard;


    @GetMapping("/")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> defaultData() throws IOException, JSONException {
        List<String> defaultCities = Arrays.asList("budapest", "barcelona", "melbourne");
        ArrayList<CitySmallCard> citySmallCards = new ArrayList<>();
        String searchCityURL = "https://api.teleport.org/api/urban_areas/slug:";
        for (String defaultCity : defaultCities){
            CitySmallCard cityCard = citySmallCard.createCitySmallCard();
            JSONObject resultName = apiCall.getResult(searchCityURL + defaultCity+"/");
            String fullName = resultName.getString("full_name");
            cityCard.setCityName(fullName);
            JSONObject resultImage = apiCall.getResult(searchCityURL + defaultCity+"/images");
            String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");
            cityCard.setImage(imageURL);
            citySmallCards.add(cityCard);
        }
        return citySmallCards;
    }


    @GetMapping("/search")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CityScores> result() throws IOException, JSONException {
        ArrayList<CityScores> listOfCitiesByScores = new ArrayList<>();
        List<String> defaultCities = Arrays.asList("budapest", "barcelona", "melbourne");
        for (String defaultCity : defaultCities) {
            ArrayList<Score> scoreArrayList = new ArrayList<>();
            CityScores cityResult = cityScores.createCityScores();
            JSONObject result = apiCall.getResult("https://api.teleport.org/api/urban_areas/slug:" + defaultCity + "/scores/");

            JSONArray categories = (JSONArray) result.get("categories");
            for (int i = 0; i < categories.length(); i++) {
                scoreArrayList.add(new Score(categories.getJSONObject(i).getString("name"), categories.getJSONObject(i).getInt("score_out_of_10")));
            }

            cityResult.setCityName(defaultCity);
            cityResult.setScores(scoreArrayList);
            listOfCitiesByScores.add(cityResult);
        }

        return listOfCitiesByScores;
    }
}
