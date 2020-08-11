package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Comment;
import com.codecool.citystatistics.entity.FavouriteCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllBySlug(String slug);
}
