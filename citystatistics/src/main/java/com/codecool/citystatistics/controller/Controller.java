package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.entity.Comment;
import com.codecool.citystatistics.entity.FavouriteCity;
import com.codecool.citystatistics.entity.Image;
import com.codecool.citystatistics.init.PreDefinedSlugSet;
import com.codecool.citystatistics.model.*;
import com.codecool.citystatistics.repository.CommentRepository;
import com.codecool.citystatistics.repository.FavouriteCityRepository;
import com.codecool.citystatistics.repository.ImageRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    ApiCall apiCall;

    @Autowired
    FavouriteCityRepository favouriteCityRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ImageRepository imageRepository;


    @GetMapping("/continent/{continent}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> continentCities(@PathVariable String continent) throws IOException, JSONException {
        ArrayList<CitySmallCard> citySmallCards = new ArrayList<>();
        String searchContinentURL = "https://api.teleport.org/api/continents/geonames%3A" + continent + "/urban_areas/";
        JSONObject resultSlugs = apiCall.getResult(searchContinentURL);
        JSONArray slugs = resultSlugs.getJSONObject("_links").getJSONArray("ua:items");

        List<String> favouriteCitySlugs = favouriteCityRepository.getAllFavouriteSlug();

        for (int i = 0; i < slugs.length(); i++) {

            JSONObject citySlug = slugs.getJSONObject(i);
            String name = citySlug.getString("name");
            String slug = citySlug.getString("href").substring(citySlug.getString("href").indexOf("slug:") + 5, citySlug.getString("href").length() - 1);

            JSONObject resultImage = apiCall.getResult(citySlug.getString("href").replaceAll("\\\\", "") + "images");
            String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");

            boolean isFavourite = favouriteCitySlugs.contains(slug);


            CitySmallCard cityCard = CitySmallCard
                    .builder()
                    .cityName(name)
                    .citySlug(slug)
                    .cityImage(imageURL)
                    .isFavourite(isFavourite)
                    .build();

            citySmallCards.add(cityCard);

        }
        return citySmallCards;
    }


    @GetMapping("/cityalldata/{cityslug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public CityAllData result(@PathVariable String cityslug) throws IOException, JSONException {
        String URL = "https://api.teleport.org/api/urban_areas/slug:" + cityslug;
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        ArrayList<Salary> salaryArrayList = new ArrayList<>();
        ArrayList<String> comments = commentRepository.getAllCommentsBySlug(cityslug);


        JSONObject resultName = apiCall.getResult(URL + "/");
        String name = resultName.getString("name");


        JSONObject resultScores = apiCall.getResult(URL + "/scores/");
        JSONArray categories = (JSONArray) resultScores.get("categories");

        for (int i = 0; i < categories.length(); i++) {
            String scoreName = categories.getJSONObject(i).getString("name");
            int scoreNumber = categories.getJSONObject(i).getInt("score_out_of_10");
            Score score = Score.builder()
                    .name(scoreName)
                    .score(scoreNumber)
                    .build();
            scoreArrayList.add(score);
        }


        JSONObject resultSalaries = apiCall.getResult(URL + "/salaries/");
        JSONArray salaries = (JSONArray) resultSalaries.get("salaries");
        for (int i = 0; i < salaries.length(); i++) {

            String title = salaries.getJSONObject(i).getJSONObject("job").getString("title");
            int percentile_25 = salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_25");
            int percentile_50 = salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_50");
            int percentile_75 = salaries.getJSONObject(i).getJSONObject("salary_percentiles").getInt("percentile_75");
            Salary salary = Salary.builder()
                    .title(title)
                    .percentile_25(percentile_25)
                    .percentile_50(percentile_50)
                    .percentile_75(percentile_75)
                    .build();
            salaryArrayList.add(salary);
        }

        JSONObject resultImage = apiCall.getResult(URL + "/images/");
        String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");


        return CityAllData
                .builder()
                .cityName(name)
                .citySlug(cityslug)
                .scores(scoreArrayList)
                .salaries(salaryArrayList)
                .image(imageURL)
                .comments(comments)
                .build();
    }

    @PostMapping("/add-favourite-city/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void addFavouriteCity(@PathVariable String citySlug) {

        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                favouriteCityRepository.save(FavouriteCity.builder().slug(citySlug).build());
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @PostMapping("/delete-favourite-city/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void deleteFavouriteCity(@PathVariable String citySlug) {

        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                favouriteCityRepository.deleteFavouriteCityBySlug(citySlug);
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @PostMapping("/add-comment/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void addComment(@PathVariable String citySlug, @RequestBody String comment) throws JSONException {
        JSONObject receivedComment = new JSONObject(comment);
        System.out.println(receivedComment.getString("comment"));
        System.out.println("query comment: " + comment);
        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                commentRepository.save(Comment.builder().slug(citySlug).comment(receivedComment.getString("comment")).build());
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @GetMapping("/get-all-favourite-cities")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> getAllFavouriteCitySlugs() throws IOException, JSONException {

        ArrayList<String> allFavouriteSlug = favouriteCityRepository.getAllFavouriteSlug();
        ArrayList<CitySmallCard> citySmallCards = new ArrayList<>();
        for (String favouriteSlug : allFavouriteSlug){
            String URL = "https://api.teleport.org/api/urban_areas/slug:"+favouriteSlug;
            JSONObject resultCityData = apiCall.getResult(URL+"/");
            String name = resultCityData.getString("name");

            JSONObject resultImage = apiCall.getResult(URL + "/images/");
            String imageURL = resultImage.getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("web");

            CitySmallCard cityCard = CitySmallCard
                    .builder()
                    .cityName(name)
                    .citySlug(favouriteSlug)
                    .cityImage(imageURL)
                    .isFavourite(true)
                    .build();


            citySmallCards.add(cityCard);
        }
        return citySmallCards;
    }


    @PostMapping("/saveimage/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void saveImage(@PathVariable String citySlug, @RequestBody String base64) throws IOException, JSONException {
        Image image = Image.builder()
                .slug(citySlug)
                .base64(base64)
                .build();

        imageRepository.save(image);

    }




}
