package com.ags.logitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para resposta de login
 * Contém o token JWT e informações básicas do usuário
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {

   private String token;
   private String tokenType = "Bearer";
   private UsuarioResponseDTO usuario;

   public LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
      this.token = token;
      this.usuario = usuario;
   }
}
