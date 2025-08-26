package com.ags.logitrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entidade que representa um usuário do sistema LogiTrack
 * Implementa UserDetails para integração com Spring Security
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank(message = "Nome de usuário é obrigatório")
   @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
   @Column(unique = true, nullable = false)
   private String username;

   @NotBlank(message = "Email é obrigatório")
   @Email(message = "Email deve ter um formato válido")
   @Column(unique = true, nullable = false)
   private String email;

   @NotBlank(message = "Senha é obrigatória")
   @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
   @Column(nullable = false)
   private String password;

   @NotBlank(message = "Nome completo é obrigatório")
   @Size(max = 100, message = "Nome completo deve ter no máximo 100 caracteres")
   @Column(nullable = false)
   private String nomeCompleto;

   @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
   private String telefone;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private TipoUsuario tipoUsuario = TipoUsuario.USUARIO;

   @Column(nullable = false)
   private Boolean ativo = true;

   @Column(nullable = false)
   private LocalDateTime dataCriacao = LocalDateTime.now();

   private LocalDateTime dataUltimoAcesso;

   // Construtor para criação rápida de usuário
   public Usuario(String username, String email, String password, String nomeCompleto, TipoUsuario tipoUsuario) {
      this.username = username;
      this.email = email;
      this.password = password;
      this.nomeCompleto = nomeCompleto;
      this.tipoUsuario = tipoUsuario;
      this.dataCriacao = LocalDateTime.now();
      this.ativo = true;
   }

   // Métodos do UserDetails para Spring Security
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return username;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return ativo;
   }

   // Enum para tipos de usuário
   public enum TipoUsuario {
      ADMIN("Administrador"),
      USUARIO("Usuário");

      private final String descricao;

      TipoUsuario(String descricao) {
         this.descricao = descricao;
      }

      public String getDescricao() {
         return descricao;
      }
   }
}
