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
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false, columnDefinition = "LONGVARCHAR")
    private String base64;

    @ManyToOne
    private AppUser user;

}
