package com.codecool.citystatistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCredentials {

    private String username;

    private String password;

    private String email;

    private LocalDate birthDate;

}
