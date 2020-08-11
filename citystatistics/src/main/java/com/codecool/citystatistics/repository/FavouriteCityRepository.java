package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.FavouriteCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface FavouriteCityRepository extends JpaRepository<FavouriteCity, Long> {
    int deleteFavouriteCityBySlug(@Param("slug") String slug);

}
