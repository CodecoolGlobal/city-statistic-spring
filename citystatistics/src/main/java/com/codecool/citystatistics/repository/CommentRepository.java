package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT comment FROM Comment WHERE slug='budapest'")
    @Modifying(clearAutomatically=true)
    List<String> getAllCommentsBySlug();
}
