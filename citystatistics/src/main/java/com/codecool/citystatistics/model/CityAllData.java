package com.codecool.citystatistics.model;

import java.util.ArrayList;

public class CityAllData {
    public String cityName;
    public String citySlug;
    public String image;
    public ArrayList<Score> scores;
    public ArrayList<Salary> salaries;


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCitySlug(String citySlug) {
        this.citySlug = citySlug;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

    public void setSalaries(ArrayList<Salary> salaries) {
        this.salaries = salaries;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
