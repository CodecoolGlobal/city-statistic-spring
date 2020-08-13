package com.codecool.citystatistics.repository;


import com.codecool.citystatistics.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT base64 FROM Image WHERE slug=:citySlug")
    ArrayList<String> getAllBase64BySlug(@Param("citySlug") String citySlug);
}
