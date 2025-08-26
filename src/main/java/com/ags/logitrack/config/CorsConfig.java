package com.ags.logitrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Para desenvolvimento, permite todas as origens
        config.setAllowCredentials(false);
        config.addAllowedOriginPattern("*");

        // Ou, para produção, use credenciais com origens específicas:
        // config.setAllowCredentials(true);
        // config.addAllowedOrigin("http://localhost:8081");
        // config.addAllowedOrigin("http://localhost:8082");
        // config.addAllowedOrigin("http://127.0.0.1:8082");
        // config.addAllowedOrigin("http://localhost:19006");
        // config.addAllowedOrigin("http://127.0.0.1:19006");

        // Permite todos os cabeçalhos
        config.addAllowedHeader("*");

        // Permite todos os métodos (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
