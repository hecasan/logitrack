package com.ags.logitrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de segurança da aplicação
 * Define as regras de autenticação e autorização
 * Configura o filtro JWT e endpoints públicos
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

   private final JwtAuthenticationFilter jwtAuthFilter;
   private final AuthenticationProvider authenticationProvider;

   /**
    * Configura a cadeia de filtros de segurança
    * Define quais endpoints são públicos e quais requerem autenticação
    */
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
            // Desabilita CSRF pois usamos JWT (stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Configura CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Configura autorização de requisições
            .authorizeHttpRequests(auth -> auth
                  // Endpoints públicos (não requerem autenticação)
                  .requestMatchers("/api/auth/**").permitAll()
                  .requestMatchers("/h2-console/**").permitAll()
                  .requestMatchers("/error").permitAll()

                  // Endpoints que requerem role ADMIN
                  .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                  .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")
                  .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMIN")
                  .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")

                  // Endpoints que requerem autenticação (qualquer usuário logado)
                  .requestMatchers("/api/usuarios/perfil").authenticated()
                  .requestMatchers("/api/robos/**").authenticated()
                  .requestMatchers("/api/entregas/**").authenticated()
                  .requestMatchers("/api/sensores/**").authenticated()

                  // Todas as outras requisições requerem autenticação
                  .anyRequest().authenticated())

            // Configuração de sessão stateless (JWT)
            .sessionManagement(session -> session
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configura o provedor de autenticação
            .authenticationProvider(authenticationProvider)

            // Adiciona o filtro JWT antes do filtro de autenticação padrão
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // Configuração para H2 Console (apenas desenvolvimento)
            .headers(headers -> headers
                  .frameOptions(frameOptions -> frameOptions.disable()));

      return http.build();
   }

   /**
    * Configuração CORS para permitir requisições do frontend
    * Permite requisições de qualquer origem (configure adequadamente em produção)
    */
   @Bean
   CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();

      // Permite qualquer origem (configure adequadamente em produção)
      configuration.setAllowedOriginPatterns(List.of("*"));

      // Métodos HTTP permitidos
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

      // Cabeçalhos permitidos
      configuration.setAllowedHeaders(List.of("*"));

      // Permite credenciais
      configuration.setAllowCredentials(true);

      // Cabeçalhos expostos na resposta
      configuration.setExposedHeaders(List.of("Authorization"));

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);

      return source;
   }
}
