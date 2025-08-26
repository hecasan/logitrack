package com.ags.logitrack.dto;

import com.ags.logitrack.model.Usuario;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para resposta de usuário
 * Contém informações do usuário (sem senha) para retorno seguro
 */
@Data
public class UsuarioResponseDTO {

   private Long id;
   private String username;
   private String email;
   private String nomeCompleto;
   private String telefone;
   private Usuario.TipoUsuario tipoUsuario;
   private String tipoUsuarioDescricao;
   private Boolean ativo;
   private LocalDateTime dataCriacao;
   private LocalDateTime dataUltimoAcesso;

   // Construtor que converte de Usuario para UsuarioResponseDTO
   public UsuarioResponseDTO(Usuario usuario) {
      this.id = usuario.getId();
      this.username = usuario.getUsername();
      this.email = usuario.getEmail();
      this.nomeCompleto = usuario.getNomeCompleto();
      this.telefone = usuario.getTelefone();
      this.tipoUsuario = usuario.getTipoUsuario();
      this.tipoUsuarioDescricao = usuario.getTipoUsuario().getDescricao();
      this.ativo = usuario.getAtivo();
      this.dataCriacao = usuario.getDataCriacao();
      this.dataUltimoAcesso = usuario.getDataUltimoAcesso();
   }

   // Construtor vazio necessário para serialização
   public UsuarioResponseDTO() {
   }
}
