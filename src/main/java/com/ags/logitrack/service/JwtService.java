package com.ags.logitrack.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável pela geração e validação de tokens JWT
 * Utiliza a biblioteca JJWT para manipulação de tokens
 */
@Service
public class JwtService {

   // Chave secreta para assinatura dos tokens (em produção, use uma chave mais
   // segura)
   @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
   private String secretKey;

   // Tempo de expiração do token em milissegundos (24 horas)
   @Value("${jwt.expiration:86400000}")
   private long jwtExpiration;

   /**
    * Extrai o nome de usuário do token JWT
    * 
    * @param token Token JWT
    * @return String username
    */
   public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   /**
    * Extrai uma claim específica do token
    * 
    * @param token          Token JWT
    * @param claimsResolver Função para extrair a claim
    * @param <T>            Tipo da claim
    * @return T claim extraída
    */
   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   /**
    * Gera um token JWT para o usuário
    * 
    * @param userDetails Detalhes do usuário
    * @return String token JWT
    */
   public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
   }

   /**
    * Gera um token JWT com claims adicionais
    * 
    * @param extraClaims Claims extras a serem incluídas
    * @param userDetails Detalhes do usuário
    * @return String token JWT
    */
   public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
      return buildToken(extraClaims, userDetails, jwtExpiration);
   }

   /**
    * Obtém o tempo de expiração configurado
    * 
    * @return long tempo de expiração em milissegundos
    */
   public long getExpirationTime() {
      return jwtExpiration;
   }

   /**
    * Constrói o token JWT
    * 
    * @param extraClaims Claims extras
    * @param userDetails Detalhes do usuário
    * @param expiration  Tempo de expiração
    * @return String token JWT
    */
   private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
      return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
   }

   /**
    * Valida se o token é válido para o usuário
    * 
    * @param token       Token JWT
    * @param userDetails Detalhes do usuário
    * @return boolean true se válido
    */
   public boolean isTokenValid(String token, UserDetails userDetails) {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
   }

   /**
    * Verifica se o token está expirado
    * 
    * @param token Token JWT
    * @return boolean true se expirado
    */
   private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   /**
    * Extrai a data de expiração do token
    * 
    * @param token Token JWT
    * @return Date data de expiração
    */
   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   /**
    * Extrai todas as claims do token
    * 
    * @param token Token JWT
    * @return Claims todas as claims
    */
   private Claims extractAllClaims(String token) {
      return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
   }

   /**
    * Obtém a chave de assinatura
    * 
    * @return Key chave de assinatura
    */
   private Key getSignInKey() {
      byte[] keyBytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(keyBytes);
   }
}
