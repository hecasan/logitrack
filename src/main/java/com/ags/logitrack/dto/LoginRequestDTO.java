package com.ags.logitrack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para requisição de login
 * Contém as credenciais do usuário para autenticação
 */
@Data
public class LoginRequestDTO {

   @NotBlank(message = "Nome de usuário é obrigatório")
   private String username;

   @NotBlank(message = "Senha é obrigatória")
   private String password;
}
