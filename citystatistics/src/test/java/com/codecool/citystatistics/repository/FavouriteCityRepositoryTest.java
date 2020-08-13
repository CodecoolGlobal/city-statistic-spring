package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.FavouriteCity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class FavouriteCityRepositoryTest {

    @Autowired
    FavouriteCityRepository favouriteCityRepository;

    @Test
    public void saveOnSimple(){
        FavouriteCity favouriteCity = FavouriteCity
                .builder()
                .slug("budapest")
                .build();

        favouriteCityRepository.save(favouriteCity);

        assertThat(favouriteCityRepository.findAll()).hasSize(1);
    }

    @Test
    public void saveUniqueFieldTwice(){
        FavouriteCity favouriteCity = FavouriteCity
                .builder()
                .slug("budapest")
                .build();

        favouriteCityRepository.save(favouriteCity);

        FavouriteCity favouriteCity2 = FavouriteCity
                .builder()
                .slug("budapest")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {favouriteCityRepository.saveAndFlush(favouriteCity2);});
    }

    @Test
    public void slugShouldBeNotNull(){
        FavouriteCity favouriteCity = FavouriteCity
                .builder()
                .build();

        assertThrows(DataIntegrityViolationException.class, ()-> {favouriteCityRepository.save(favouriteCity);});
    }

    @Test
    public void giveBackAllFavouriteSlug(){
        FavouriteCity favouriteCity1 = FavouriteCity
                .builder()
                .slug("budapest")
                .build();

        FavouriteCity favouriteCity2 = FavouriteCity
                .builder()
                .slug("london")
                .build();

        favouriteCityRepository.saveAll(Lists.newArrayList(favouriteCity1, favouriteCity2));

        assertThat(favouriteCityRepository.getAllFavouriteSlug()).hasSize(2).containsOnlyOnce("budapest", "london");

    }

    @Test
    public void deleteFavouriteSlug(){
        FavouriteCity favouriteCity1 = FavouriteCity
                .builder()
                .slug("budapest")
                .build();

        FavouriteCity favouriteCity2 = FavouriteCity
                .builder()
                .slug("london")
                .build();

        favouriteCityRepository.saveAll(Lists.newArrayList(favouriteCity1, favouriteCity2));
        favouriteCityRepository.deleteFavouriteCityBySlug("budapest");
        assertThat(favouriteCityRepository.getAllFavouriteSlug()).hasSize(1).contains("london");

    }
}