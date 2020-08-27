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
public class FavouriteCity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String slug;


    @ManyToOne
    private AppUser user;
}
