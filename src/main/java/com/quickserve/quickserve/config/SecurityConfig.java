package com.quickserve.quickserve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF off for Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // <--- allow register/login
                        .anyRequest().authenticated()             // rest protected
                )
                .formLogin(form -> form.disable()); // default login page off

        return http.build();
    }
}