package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.AppUser;
import com.codecool.citystatistics.entity.FavouriteCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;

public interface FavouriteCityRepository extends JpaRepository<FavouriteCity, Long> {
    @Transactional
    void deleteFavouriteCityBySlugAndUser(String citySlug, AppUser appUser);

    @Query("SELECT slug FROM FavouriteCity WHERE user = :user")
    ArrayList<String> getFavouriteCityByUser(@Param ("user") AppUser user);

}
