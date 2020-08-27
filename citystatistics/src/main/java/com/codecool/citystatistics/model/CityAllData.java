package com.codecool.citystatistics.model;

import com.codecool.citystatistics.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityAllData {
    public String cityName;
    public String citySlug;
    public String image;
    public ArrayList<Score> scores;
    public ArrayList<Salary> salaries;
    public ArrayList<Comment> comments;
    public ArrayList<String> images;

}
