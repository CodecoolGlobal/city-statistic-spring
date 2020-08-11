package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.FavouriteCity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface FavouriteCityRepository extends JpaRepository<FavouriteCity, Long> {
    @Transactional
    void deleteFavouriteCityBySlug(String citySlug);
}
