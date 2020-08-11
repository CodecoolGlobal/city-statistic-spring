package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.FavouriteCity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteCityRepository extends JpaRepository<FavouriteCity, Long> {

}
