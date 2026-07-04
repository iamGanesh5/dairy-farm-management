package com.dairy.farm.management.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config =
                new CorsConfiguration();

        /*
         * Allow React Frontend
         */
        config.addAllowedOrigin(
                "http://localhost:3000"
        );

        config.addAllowedOrigin(
                "http://localhost:3001"
        );

        /*
         * Allow Headers
         */
        config.addAllowedHeader("*");

        /*
         * Allow Methods
         */
        config.addAllowedMethod("*");

        /*
         * Allow JWT/Auth
         */
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return new CorsFilter(source);

    }

}