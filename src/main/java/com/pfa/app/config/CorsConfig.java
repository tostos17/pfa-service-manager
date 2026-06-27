package com.pfa.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Configure allowed origins dynamically from your properties
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // 2. Specify allowed HTTP Methods for REST architectures
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 3. Permit standard security and content negotiation headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control", "X-Requested-With"));

        // 4. Expose headers if your frontend needs access to specific parts of metadata response
        configuration.setExposedHeaders(List.of("Authorization"));

        // 5. Explicitly allow credentials like Cookies or Authorization headers over cross-origin paths
        configuration.setAllowCredentials(true);

        // 6. Define pre-flight request max age cache duration (Max 1 hour)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply globally to all controller pathways

        return new CorsFilter(source);
    }
}