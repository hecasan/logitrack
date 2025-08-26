package com.ags.logitrack.config;

import com.ags.logitrack.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta todas as requisições HTTP
 * Valida o token JWT presente no cabeçalho Authorization
 * Se válido, autentica o usuário no contexto de segurança
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtService jwtService;
   private final UserDetailsService userDetailsService;

   @Override
   protected void doFilterInternal(
         @NonNull HttpServletRequest request,
         @NonNull HttpServletResponse response,
         @NonNull FilterChain filterChain) throws ServletException, IOException {

      // Se for uma requisição para endpoints públicos, pula a autenticação
      if (isPublicEndpoint(request.getServletPath())) {
         filterChain.doFilter(request, response);
         return;
      }

      // Extrai o cabeçalho Authorization
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String username;

      // Verifica se o cabeçalho contém um token Bearer
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
      }

      // Extrai o token JWT (remove "Bearer " do início)
      jwt = authHeader.substring(7);
      username = jwtService.extractUsername(jwt);

      // Se o username foi extraído e não há autenticação ativa
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

         // Carrega os detalhes do usuário do banco de dados
         UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

         // Valida o token
         if (jwtService.isTokenValid(jwt, userDetails)) {

            // Cria o token de autenticação
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities());

            // Define detalhes da requisição
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Define a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authToken);
         }
      }

      // Continue com a cadeia de filtros
      filterChain.doFilter(request, response);
   }

   /**
    * Verifica se o endpoint é público (não requer autenticação)
    * 
    * @param path Caminho da requisição
    * @return boolean true se for público
    */
   private boolean isPublicEndpoint(String path) {
      return path.startsWith("/api/auth/") ||
            path.startsWith("/h2-console/") ||
            path.equals("/") ||
            path.startsWith("/error");
   }
}
