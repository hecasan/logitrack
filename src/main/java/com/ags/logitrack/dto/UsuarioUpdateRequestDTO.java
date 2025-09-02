package com.ags.logitrack.dto;

import com.ags.logitrack.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de atualização de usuário
 * Contém dados para atualizar um usuário existente (senha opcional)
 */
public class UsuarioUpdateRequestDTO {

   @NotBlank(message = "Nome de usuário é obrigatório")
   @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
   private String username;

   @NotBlank(message = "Email é obrigatório")
   @Email(message = "Email deve ter um formato válido")
   private String email;

   // Senha opcional para atualização - se não fornecida, mantém a atual
   @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
   private String password;

   @NotBlank(message = "Nome completo é obrigatório")
   @Size(max = 100, message = "Nome completo deve ter no máximo 100 caracteres")
   private String nomeCompleto;

   @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
   private String telefone;

   private Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.USUARIO;

   // Construtores
   public UsuarioUpdateRequestDTO() {}

   // Getters e Setters
   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getNomeCompleto() {
      return nomeCompleto;
   }

   public void setNomeCompleto(String nomeCompleto) {
      this.nomeCompleto = nomeCompleto;
   }

   public String getTelefone() {
      return telefone;
   }

   public void setTelefone(String telefone) {
      this.telefone = telefone;
   }

   public Usuario.TipoUsuario getTipoUsuario() {
      return tipoUsuario;
   }

   public void setTipoUsuario(Usuario.TipoUsuario tipoUsuario) {
      this.tipoUsuario = tipoUsuario;
   }
}
