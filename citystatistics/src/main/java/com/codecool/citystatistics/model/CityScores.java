package com.codecool.citystatistics.model;

import java.util.ArrayList;

public class CityScores {
    public String cityName;
    public ArrayList<Score> scores;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }


}
