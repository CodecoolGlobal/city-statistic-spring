package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.entity.AppUser;
import com.codecool.citystatistics.entity.Comment;
import com.codecool.citystatistics.entity.FavouriteCity;
import com.codecool.citystatistics.entity.Image;
import com.codecool.citystatistics.init.PreDefinedSlugSet;
import com.codecool.citystatistics.model.*;
import com.codecool.citystatistics.repository.AppUserRepository;
import com.codecool.citystatistics.repository.CommentRepository;
import com.codecool.citystatistics.repository.FavouriteCityRepository;
import com.codecool.citystatistics.repository.ImageRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class Controller {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ApiCall apiCall;

    @Autowired
    FavouriteCityRepository favouriteCityRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    AppUserRepository appUserRepository;


    @GetMapping("/continent/{continent}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> continentCities(@PathVariable String continent) throws IOException, JSONException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());

        ArrayList<CitySmallCard> citySmallCards = new ArrayList<>();
        String searchContinentURL = "https://api.teleport.org/api/continents/geonames%3A" + continent + "/urban_areas/";
        JSONObject resultSlugs = apiCall.getResult(searchContinentURL);
        JSONArray slugs = resultSlugs.getJSONObject("_links").getJSONArray("ua:items");

        List<String> favouriteCitySlugs = favouriteCityRepository.getFavouriteCityByUser(appUser);

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


    @GetMapping("/cityalldata/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public CityAllData result(@PathVariable String citySlug) throws IOException, JSONException {
        String URL = "https://api.teleport.org/api/urban_areas/slug:" + citySlug;
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        ArrayList<Salary> salaryArrayList = new ArrayList<>();
        ArrayList<Comment> comments = commentRepository.getCommentsBySlug(citySlug);
        ArrayList<String> cityImages = imageRepository.getAllBase64BySlug(citySlug);


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
                .citySlug(citySlug)
                .scores(scoreArrayList)
                .salaries(salaryArrayList)
                .image(imageURL)
                .comments(comments)
                .images(cityImages)
                .build();
    }

    @PostMapping("/add-favourite-city/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void addFavouriteCity(@PathVariable String citySlug) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());


        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                favouriteCityRepository.save(FavouriteCity.builder()
                        .slug(citySlug)
                        .user(appUser)
                        .build());
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @PostMapping("/delete-favourite-city/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void deleteFavouriteCity(@PathVariable String citySlug) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());



        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                favouriteCityRepository.deleteFavouriteCityBySlugAndUser(citySlug, appUser);
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @PostMapping("/add-comment/{citySlug}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void addComment(@PathVariable String citySlug, @RequestBody String comment) throws JSONException {
        JSONObject receivedComment = new JSONObject(comment);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());

        System.out.println(receivedComment.getString("comment"));
        System.out.println("query comment: " + comment);
        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                commentRepository.saveAndFlush(Comment.builder()
                        .slug(citySlug)
                        .comment(receivedComment
                        .getString("comment"))
                        .upvote(10)
                        .downvote(0)
                        .appuser(appUser)
                        .build());
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
    }

    @GetMapping("/get-all-favourite-cities")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public ArrayList<CitySmallCard> getAllFavouriteCitySlugs() throws IOException, JSONException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());


        ArrayList<String> allFavouriteSlug = favouriteCityRepository.getFavouriteCityByUser(appUser);
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
        JSONObject imageBase64 = new JSONObject(base64);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());

        try {
            if (PreDefinedSlugSet.preDefinedSlugSet.contains(citySlug)) {
                imageRepository.save(Image.builder().slug(citySlug).user(appUser).base64(imageBase64.getString("base64")).build());
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error: " + e);
        }
     }


    @GetMapping("/profile")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public AppUser getMyProfile() throws IOException, JSONException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.getAppUserByUsername((String) authentication.getPrincipal());

        AppUser returnAppUser = AppUser.builder()
                                    .email(appUser.getEmail())
                                    .username(appUser.getUsername())
                                    .profileImage(appUser.getProfileImage())
                                    .build();

        return returnAppUser;
    }

    @PutMapping("/rate/{commentID}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void rateComment(@PathVariable Long commentID, @RequestBody String rate) throws JSONException {
        JSONObject rating = new JSONObject(rate);
        Comment comment = commentRepository.getOne(commentID);
        if (rating.getString("rate").equals("upvote")) {
            comment.setUpvote(comment.getUpvote() + 1);
        }
        else {
            comment.setDownvote(comment.getDownvote() + 1);
        }
        commentRepository.save(comment);
    }
    @PostMapping("/reply/{commentID}")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
    public void replyComment(@PathVariable Long commentID, @RequestBody String reply) throws JSONException {
        JSONObject replies = new JSONObject(reply);
        Comment comment = commentRepository.getOne(commentID);
        List<String> previousReplies = comment.getReplies();
        previousReplies.add(replies.getString("reply"));
        comment.setReplies(previousReplies);

        commentRepository.saveAndFlush(comment);

    }

}
