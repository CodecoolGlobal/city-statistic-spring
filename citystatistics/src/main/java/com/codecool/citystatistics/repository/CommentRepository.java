package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    ArrayList<Comment> getCommentsBySlug(String slug);
}
