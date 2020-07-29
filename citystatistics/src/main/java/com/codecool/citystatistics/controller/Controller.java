package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.instance_creator.*;
import com.codecool.citystatistics.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    ScoreCreator scoreCreator;

    @Autowired
    CityScoresCreator cityScores;

    @Autowired
    CitySmallCardCreator citySmallCard;

    @Autowired
    CityAllDataCreator cityAllDataCreator;

    @Autowired
    SalaryCreator salaryCreator;

    @GetMapping("/continent/{continent}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> continentCities(@PathVariable String continent) throws IOException, JSONException {
        ArrayList<CitySmallCard> citySmallCards = new ArrayList<>();
        String searchContinentURL = "https://api.teleport.org/api/continents/geonames%3A"+continent+"/urban_areas/";
        JSONObject resultSlugs = apiCall.getResult(searchContinentURL);
        JSONArray slugs = resultSlugs.getJSONObject("_links").getJSONArray("ua:items");
        for (int i = 0; i < slugs.length(); i++){
            System.out.println(slugs.get(i));
            CitySmallCard cityCard = citySmallCard.createCitySmallCard();
            JSONObject citySlug = slugs.getJSONObject(i);
            System.out.println(citySlug);
            cityCard.setCityName(citySlug.getString("name"));
            cityCard.setCitySlug(citySlug.getString("href").substring(citySlug.getString("href").indexOf("slug:")+5, citySlug.getString("href").length()-1));
            JSONObject resultImage = apiCall.getResult(citySlug.getString("href").replaceAll("\\\\", "")+"images");
            String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");
            cityCard.setImage(imageURL);
            citySmallCards.add(cityCard);

        }
        return citySmallCards;
    }



    @GetMapping("/cityalldata/{cityslug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public CityAllData result(@PathVariable String cityslug) throws IOException, JSONException {
        String URL = "https://api.teleport.org/api/urban_areas/slug:"+cityslug;
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        ArrayList<Salary> salaryArrayList = new ArrayList<>();
        CityAllData cityAllData = cityAllDataCreator.createCityAllData();

        cityAllData.setCitySlug(cityslug);

        JSONObject resultName = apiCall.getResult(URL  + "/");
        cityAllData.setCityName(resultName.getString("name"));

        JSONObject resultScores = apiCall.getResult(URL  + "/scores/");
        JSONArray categories = (JSONArray) resultScores.get("categories");
        for (int i = 0; i < categories.length(); i++) {
            Score score = scoreCreator.createScore();
            score.setName(categories.getJSONObject(i).getString("name"));
            score.setScore(categories.getJSONObject(i).getInt("score_out_of_10"));
            scoreArrayList.add(score);
        }
        cityAllData.setScores(scoreArrayList);

        JSONObject resultSalaries = apiCall.getResult(URL  + "/salaries/");
        JSONArray salaries = (JSONArray) resultSalaries.get("salaries");
        for (int i = 0; i < salaries.length(); i++) {
            Salary salary = salaryCreator.createSalary();
            salary.setTitle(salaries.getJSONObject(i).getJSONObject("job").getString("title"));
            salary.setPercentile_25(salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_25"));
            salary.setPercentile_50(salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_50"));
            salary.setPercentile_75(salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_75"));
            salaryArrayList.add(salary);
        }
        cityAllData.setSalaries(salaryArrayList);

        JSONObject resultImage = apiCall.getResult(URL + "/images/");
        String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");
        cityAllData.setImage(imageURL);

        return cityAllData;
    }
}

/*
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


    @GetMapping("/defaultCities")
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
                Score score = scoreCreator.createScore();
                score.setName(categories.getJSONObject(i).getString("name"));
                score.setScore(categories.getJSONObject(i).getInt("score_out_of_10"));
                scoreArrayList.add(score);
            }
            cityResult.setCityName(defaultCity);
            cityResult.setScores(scoreArrayList);
            listOfCitiesByScores.add(cityResult);
        }

        return listOfCitiesByScores;
    }
 */
