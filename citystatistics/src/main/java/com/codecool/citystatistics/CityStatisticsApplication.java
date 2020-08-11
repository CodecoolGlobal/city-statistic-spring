package com.codecool.citystatistics;

import com.codecool.citystatistics.entity.FavouriteCity;
import com.codecool.citystatistics.repository.FavouriteCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CityStatisticsApplication {



    public static void main(String[] args) {
        SpringApplication.run(CityStatisticsApplication.class, args);
    }

}
