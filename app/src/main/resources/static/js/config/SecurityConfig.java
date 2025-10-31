package com.yourproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/adminDashboard/**").hasRole("ADMIN")
            .requestMatchers("/doctorDashboard/**").hasRole("DOCTOR")
            .anyRequest().permitAll()
            .and()
            .formLogin().and().logout();
        return http.build();
    }
}
