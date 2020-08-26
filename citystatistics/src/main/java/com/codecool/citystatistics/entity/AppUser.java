package com.codecool.citystatistics.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(nullable = false)
    private String email;

    private LocalDate birthDate;

    private Long age = calculateAge();

    @OneToMany(
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();


    public Long calculateAge(){
        if(birthDate != null){
            return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        }
        return null;
    }
}
