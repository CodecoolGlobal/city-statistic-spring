package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.CitySmallCard;
import org.springframework.stereotype.Component;

@Component
public class CitySmallCardCreator {
    CitySmallCard createCitySmallCard(){
        return new CitySmallCard();
    }
}
