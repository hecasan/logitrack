package com.ags.logitrack.dto;

import com.ags.logitrack.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para requisição de criação/atualização de usuário
 * Contém todos os dados necessários para criar um novo usuário
 */
@Data
public class UsuarioRequestDTO {

   @NotBlank(message = "Nome de usuário é obrigatório")
   @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
   private String username;

   @NotBlank(message = "Email é obrigatório")
   @Email(message = "Email deve ter um formato válido")
   private String email;

   @NotBlank(message = "Senha é obrigatória")
   @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
   private String password;

   @NotBlank(message = "Nome completo é obrigatório")
   @Size(max = 100, message = "Nome completo deve ter no máximo 100 caracteres")
   private String nomeCompleto;

   @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
   private String telefone;

   private Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.USUARIO;
}
