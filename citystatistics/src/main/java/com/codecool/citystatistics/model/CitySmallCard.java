package com.codecool.citystatistics.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitySmallCard {
    public String cityName;
    public String cityImage;
    public String citySlug;
    public boolean isFavourite;


}
