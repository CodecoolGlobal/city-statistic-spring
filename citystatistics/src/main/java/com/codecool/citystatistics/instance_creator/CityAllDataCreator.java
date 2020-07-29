package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.CityAllData;
import org.springframework.stereotype.Component;

@Component
public class CityAllDataCreator {
    public CityAllData createCityAllData(){
        return  new CityAllData();
    }
}
