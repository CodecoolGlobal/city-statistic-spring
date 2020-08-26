package com.codecool.citystatistics.controller;

import com.codecool.citystatistics.entity.AppUser;
import com.codecool.citystatistics.model.RegistrationCredentials;
import com.codecool.citystatistics.model.UserCredentials;
import com.codecool.citystatistics.repository.AppUserRepository;
import com.codecool.citystatistics.security.JwtTokenServices;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    private final AppUserRepository userRepository;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public AuthController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices, AppUserRepository users) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
        this.userRepository = users;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegistrationCredentials data,  HttpServletResponse response){
        try{
            AppUser newUser = AppUser
                    .builder()
                    .username(data.getUsername())
                    .password(passwordEncoder.encode(data.getPassword()))
                    .email(data.getEmail())
                    .roles(Arrays.asList("ROLE_USER"))
                    .build();

            if(data.getBirthDate() != null){
                newUser.setBirthDate(data.getBirthDate());
                newUser.setAge(newUser.calculateAge());
            }

            userRepository.save(newUser);

            String token = jwtTokenServices.createToken(newUser.getUsername(), newUser.getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", newUser.getUsername());
            model.put("roles", newUser.getRoles());
            model.put("status", "ok");
            model.put("token", token);

            return ResponseEntity.ok(model);
        }
        catch (DataIntegrityViolationException e){
            Map<Object, Object> model = new HashMap<>();
            model.put("status", "failed");
            return ResponseEntity.unprocessableEntity().body(model);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserCredentials data) {
        try {
            String username = data.getUsername();
            // authenticationManager.authenticate calls loadUserByUsername in CustomUserDetailsService
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(username, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("roles", roles);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
    @GetMapping("/me")
    public String currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal() + "\n" + authentication.getAuthorities();
    }
}
