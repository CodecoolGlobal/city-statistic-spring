package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.CityScores;
import org.springframework.stereotype.Component;

@Component
public class CityScoresCreator {
    public CityScores createCityScores(){
        return new CityScores();
    }
}
