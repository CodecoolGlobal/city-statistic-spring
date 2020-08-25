package com.codecool.citystatistics;

import com.codecool.citystatistics.entity.AppUser;
import com.codecool.citystatistics.entity.FavouriteCity;
import com.codecool.citystatistics.repository.AppUserRepository;
import com.codecool.citystatistics.repository.FavouriteCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class CityStatisticsApplication {

    @Autowired
    AppUserRepository appUserRepository;

    public static void main(String[] args) {
        SpringApplication.run(CityStatisticsApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init(){
        return args -> {
            AppUser user = AppUser.builder()
                    .username("admin").birthDate(LocalDate.of(1999,8,8))
                    .password("password").email("admin@gmail.com").roles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"))
                    .build();

            appUserRepository.save(user);
        };
    }

}
