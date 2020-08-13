package com.codecool.citystatistics.repository;

import com.codecool.citystatistics.entity.Comment;
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
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void saveOneSimple(){
        Comment comment = Comment.builder()
                .comment("This is a test case for budapest comment")
                .slug("budapest")
                .build();

        commentRepository.save(comment);

        assertThat(commentRepository.findAll()).hasSize(1);

    }

    @Test
    public void slugShouldBeNotNull(){
        Comment comment = Comment.builder()
                .comment("This is a test case")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {commentRepository.save(comment);});
    }

    @Test
    public void commentShouldBeNotNull(){
        Comment comment = Comment.builder()
                .slug("budapest")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {commentRepository.save(comment);});
    }

    @Test
    public void getAllCommentsBySlug(){
        Comment comment = Comment.builder()
                .comment("This is a test case for budapest comment")
                .slug("budapest")
                .build();

        Comment comment2 = Comment.builder()
                .comment("This is a test case for london comment")
                .slug("london")
                .build();

        Comment comment3 = Comment.builder()
                .comment("This is a test case for budapest second comment")
                .slug("budapest")
                .build();

        commentRepository.saveAll(Lists.newArrayList(comment, comment2, comment3));

        assertThat(commentRepository.getAllCommentsBySlug("budapest")).hasSize(2)
                .containsExactlyInAnyOrder(comment.getComment(), comment3.getComment());
    }
}