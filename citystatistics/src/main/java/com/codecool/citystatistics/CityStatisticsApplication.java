package com.codecool.citystatistics;

import com.codecool.citystatistics.model.RemoteURLReader;
import com.codecool.citystatistics.resource.CityStatisticsResource;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CityStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityStatisticsApplication.class, args);
    }

}
