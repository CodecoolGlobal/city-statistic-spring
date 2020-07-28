package com.codecool.citystatistics.model;

import java.util.ArrayList;

public class CityScores {
    public String cityName;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

    public ArrayList<Score> scores;
}
