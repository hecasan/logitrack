package com.ags.logitrack.controller;

import com.ags.logitrack.dto.UsuarioRequestDTO;
import com.ags.logitrack.dto.UsuarioResponseDTO;
import com.ags.logitrack.dto.UsuarioUpdateRequestDTO;
import com.ags.logitrack.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controlador de usuários
 * Gerencia operações CRUD de usuários (apenas para administradores)
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

   private final UsuarioService usuarioService;

   /**
    * Lista todos os usuários (apenas administradores)
    * 
    * @return ResponseEntity<List<UsuarioResponseDTO>> Lista de usuários
    */
   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
      List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
      return ResponseEntity.ok(usuarios);
   }

   /**
    * Busca usuário por ID (apenas administradores)
    * 
    * @param id ID do usuário
    * @return ResponseEntity<UsuarioResponseDTO> Dados do usuário
    */
   @GetMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
      UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);
      return ResponseEntity.ok(usuario);
   }

   /**
    * Cria um novo usuário (apenas administradores)
    * 
    * @param usuarioRequest Dados do usuário
    * @return ResponseEntity<UsuarioResponseDTO> Usuário criado
    */
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
      UsuarioResponseDTO usuario = usuarioService.criarUsuario(usuarioRequest);
      return ResponseEntity.ok(usuario);
   }

   /**
    * Atualiza um usuário existente (apenas administradores)
    * 
    * @param id             ID do usuário
    * @param usuarioRequest Novos dados do usuário
    * @return ResponseEntity<UsuarioResponseDTO> Usuário atualizado
    */
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
         @PathVariable Long id,
         @RequestBody UsuarioRequestDTO usuarioRequest) {
      
      System.out.println("🔄 Recebendo requisição de atualização:");
      System.out.println("  ID: " + id);
      System.out.println("  Dados: " + usuarioRequest);
      
      UsuarioResponseDTO usuario = usuarioService.atualizarUsuario(id, usuarioRequest);
      
      System.out.println("✅ Usuário atualizado com sucesso: " + usuario);
      
      return ResponseEntity.ok(usuario);
   }

   /**
    * Desativa um usuário (apenas administradores)
    * 
    * @param id ID do usuário
    * @return ResponseEntity<Void> Resposta vazia
    */
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
      usuarioService.desativarUsuario(id);
      return ResponseEntity.ok().build();
   }

   /**
    * Reativa um usuário (apenas administradores)
    * 
    * @param id ID do usuário
    * @return ResponseEntity<Void> Resposta vazia
    */
   @PutMapping("/{id}/reativar")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> reativarUsuario(@PathVariable Long id) {
      usuarioService.reativarUsuario(id);
      return ResponseEntity.ok().build();
   }

   /**
    * Obtém perfil do usuário logado (qualquer usuário autenticado)
    * 
    * @param authentication Dados de autenticação
    * @return ResponseEntity<UsuarioResponseDTO> Perfil do usuário
    */
   @GetMapping("/perfil")
   public ResponseEntity<UsuarioResponseDTO> obterPerfil(Authentication authentication) {
      String username = authentication.getName();
      UsuarioResponseDTO usuario = usuarioService.buscarPorUsername(username);
      return ResponseEntity.ok(usuario);
   }

   /**
    * Atualiza perfil do usuário logado (qualquer usuário autenticado)
    * 
    * @param usuarioRequest Novos dados do usuário
    * @param authentication Dados de autenticação
    * @return ResponseEntity<UsuarioResponseDTO> Perfil atualizado
    */
   @PutMapping("/perfil")
   public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(
         @RequestBody UsuarioRequestDTO usuarioRequest,
         Authentication authentication) {

      String username = authentication.getName();
      UsuarioResponseDTO usuarioAtual = usuarioService.buscarPorUsername(username);

      // Usuário comum não pode alterar seu próprio tipo
      if (!usuarioService.isAdmin(username)) {
         usuarioRequest.setTipoUsuario(usuarioAtual.getTipoUsuario());
      }

      UsuarioResponseDTO usuario = usuarioService.atualizarUsuario(usuarioAtual.getId(), usuarioRequest);
      return ResponseEntity.ok(usuario);
   }
}
