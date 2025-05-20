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

        // Permite credenciais
        config.setAllowCredentials(true);

        // Permite todas as origens - você pode restringir para origens específicas se necessário
        config.addAllowedOrigin("http://localhost:8081");

        // Permite todos os cabeçalhos
        config.addAllowedHeader("*");

        // Permite todos os métodos (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
