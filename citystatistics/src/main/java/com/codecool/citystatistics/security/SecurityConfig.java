package com.codecool.citystatistics.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenServices jwtTokenServices;

    public SecurityConfig(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/auth/registration").permitAll()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers("/auth/me").authenticated()
                .antMatchers("/get-all-favourite-cities").authenticated()
                .antMatchers("/add-favourite-city/**").authenticated()
                .antMatchers("/delete-favourite-city/**").authenticated()
                .antMatchers("/saveimage/**").authenticated()
                .antMatchers("/add-comment/**").permitAll()
                .antMatchers("/rate/**").permitAll()
                .antMatchers("/reply/**").permitAll()
                .antMatchers("/profile").authenticated()
                .antMatchers("/continent/**").permitAll()
                .antMatchers("/cityalldata/**").permitAll()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class);
    }
}
