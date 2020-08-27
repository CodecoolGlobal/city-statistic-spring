package com.codecool.citystatistics.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String slug;

    @Column
    @Builder.Default
    private Integer upvote = 0;

    @Column
    @Builder.Default
    private Integer downvote = 0;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private AppUser appuser;
}
