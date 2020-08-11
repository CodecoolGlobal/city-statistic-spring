package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(" SELECT Comment.comment from Comment WHERE slug=:slug")
    List<Comment> getAllCommentsBySlug(String slug);
}
