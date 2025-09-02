package com.ags.logitrack.service;

import com.ags.logitrack.dto.UsuarioRequestDTO;
import com.ags.logitrack.dto.UsuarioResponseDTO;
import com.ags.logitrack.dto.UsuarioUpdateRequestDTO;
import com.ags.logitrack.model.Usuario;
import com.ags.logitrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de usuários
 * Contém a lógica de negócio para operações com usuários
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

   private final UsuarioRepository usuarioRepository;
   private final PasswordEncoder passwordEncoder;

   /**
    * Lista todos os usuários ativos
    * 
    * @return List<UsuarioResponseDTO> lista de usuários
    */
   @Transactional(readOnly = true)
   public List<UsuarioResponseDTO> listarUsuarios() {
      return usuarioRepository.findByAtivoTrue()
            .stream()
            .map(UsuarioResponseDTO::new)
            .collect(Collectors.toList());
   }

   /**
    * Busca um usuário por ID
    * 
    * @param id ID do usuário
    * @return UsuarioResponseDTO dados do usuário
    * @throws RuntimeException se usuário não encontrado
    */
   @Transactional(readOnly = true)
   public UsuarioResponseDTO buscarPorId(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
      return new UsuarioResponseDTO(usuario);
   }

   /**
    * Busca um usuário por username
    * 
    * @param username Nome de usuário
    * @return UsuarioResponseDTO dados do usuário
    * @throws RuntimeException se usuário não encontrado
    */
   @Transactional(readOnly = true)
   public UsuarioResponseDTO buscarPorUsername(String username) {
      Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
      return new UsuarioResponseDTO(usuario);
   }

   /**
    * Cria um novo usuário
    * 
    * @param usuarioRequest Dados do usuário a ser criado
    * @return UsuarioResponseDTO usuário criado
    * @throws RuntimeException se username ou email já existir
    */
   public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioRequest) {
      // Verifica se username já existe
      if (usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
         throw new RuntimeException("Nome de usuário já existe: " + usuarioRequest.getUsername());
      }

      // Verifica se email já existe
      if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
         throw new RuntimeException("Email já existe: " + usuarioRequest.getEmail());
      }

      // Cria novo usuário
      Usuario usuario = new Usuario();
      usuario.setUsername(usuarioRequest.getUsername());
      usuario.setEmail(usuarioRequest.getEmail());
      usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
      usuario.setNomeCompleto(usuarioRequest.getNomeCompleto());
      usuario.setTelefone(usuarioRequest.getTelefone());
      usuario.setTipoUsuario(usuarioRequest.getTipoUsuario());
      usuario.setAtivo(true);
      usuario.setDataCriacao(LocalDateTime.now());

      Usuario usuarioSalvo = usuarioRepository.save(usuario);
      return new UsuarioResponseDTO(usuarioSalvo);
   }

   /**
    * Atualiza um usuário existente
    * 
    * @param id             ID do usuário
    * @param usuarioRequest Novos dados do usuário
    * @return UsuarioResponseDTO usuário atualizado
    * @throws RuntimeException se usuário não encontrado ou dados inválidos
    */
   public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO usuarioRequest) {
      System.out.println("🔄 UsuarioService.atualizarUsuario - iniciando:");
      System.out.println("  ID: " + id);
      System.out.println("  Request: " + usuarioRequest);
      
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

      System.out.println("  Usuario encontrado: " + usuario.getUsername());

      // Verifica se username já existe (excluindo o usuário atual)
      if (!usuario.getUsername().equals(usuarioRequest.getUsername()) &&
            usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
         throw new RuntimeException("Nome de usuário já existe: " + usuarioRequest.getUsername());
      }

      // Verifica se email já existe (excluindo o usuário atual)
      if (!usuario.getEmail().equals(usuarioRequest.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
         throw new RuntimeException("Email já existe: " + usuarioRequest.getEmail());
      }

      // Atualiza dados
      usuario.setUsername(usuarioRequest.getUsername());
      usuario.setEmail(usuarioRequest.getEmail());
      
      // Só atualiza a senha se foi fornecida e não está vazia
      if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().trim().isEmpty()) {
         System.out.println("  Atualizando senha...");
         usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
      } else {
         System.out.println("  Mantendo senha atual...");
      }
      
      usuario.setNomeCompleto(usuarioRequest.getNomeCompleto());
      usuario.setTelefone(usuarioRequest.getTelefone());
      usuario.setTipoUsuario(usuarioRequest.getTipoUsuario());

      Usuario usuarioSalvo = usuarioRepository.save(usuario);
      UsuarioResponseDTO response = new UsuarioResponseDTO(usuarioSalvo);
      
      System.out.println("✅ Usuário salvo com sucesso: " + response);
      
      return response;
   }

   /**
    * Desativa um usuário (soft delete)
    * 
    * @param id ID do usuário
    * @throws RuntimeException se usuário não encontrado ou for o último admin
    */
   public void desativarUsuario(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

      // Não permite desativar o último administrador
      if (usuario.getTipoUsuario() == Usuario.TipoUsuario.ADMIN) {
         long totalAdmins = usuarioRepository.countAdministradores();
         if (totalAdmins <= 1) {
            throw new RuntimeException("Não é possível desativar o último administrador do sistema");
         }
      }

      usuario.setAtivo(false);
      usuarioRepository.save(usuario);
   }

   /**
    * Reativa um usuário
    * 
    * @param id ID do usuário
    */
   public void reativarUsuario(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

      usuario.setAtivo(true);
      usuarioRepository.save(usuario);
   }

   /**
    * Atualiza a data do último acesso do usuário
    * 
    * @param username Nome de usuário
    */
   public void atualizarUltimoAcesso(String username) {
      usuarioRepository.findByUsername(username)
            .ifPresent(usuario -> {
               usuario.setDataUltimoAcesso(LocalDateTime.now());
               usuarioRepository.save(usuario);
            });
   }

   /**
    * Atualiza um usuário existente usando UsuarioUpdateRequestDTO (senha opcional)
    * 
    * @param id             ID do usuário
    * @param usuarioRequest Novos dados do usuário
    * @return UsuarioResponseDTO usuário atualizado
    * @throws RuntimeException se usuário não encontrado ou dados inválidos
    */
   public UsuarioResponseDTO atualizarUsuarioComUpdate(Long id, UsuarioUpdateRequestDTO usuarioRequest) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

      // Verifica se username já existe (excluindo o usuário atual)
      if (!usuario.getUsername().equals(usuarioRequest.getUsername()) &&
            usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
         throw new RuntimeException("Nome de usuário já existe: " + usuarioRequest.getUsername());
      }

      // Verifica se email já existe (excluindo o usuário atual)
      if (!usuario.getEmail().equals(usuarioRequest.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
         throw new RuntimeException("Email já existe: " + usuarioRequest.getEmail());
      }

      // Atualiza dados
      usuario.setUsername(usuarioRequest.getUsername());
      usuario.setEmail(usuarioRequest.getEmail());
      
      // Só atualiza a senha se foi fornecida
      if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().trim().isEmpty()) {
         usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
      }
      
      usuario.setNomeCompleto(usuarioRequest.getNomeCompleto());
      usuario.setTelefone(usuarioRequest.getTelefone());
      usuario.setTipoUsuario(usuarioRequest.getTipoUsuario());

      Usuario usuarioSalvo = usuarioRepository.save(usuario);
      return new UsuarioResponseDTO(usuarioSalvo);
   }

   /**
    * Verifica se um usuário é administrador
    * 
    * @param username Nome de usuário
    * @return boolean true se for admin
    */
   @Transactional(readOnly = true)
   public boolean isAdmin(String username) {
      return usuarioRepository.findByUsername(username)
            .map(usuario -> usuario.getTipoUsuario() == Usuario.TipoUsuario.ADMIN)
            .orElse(false);
   }
}
