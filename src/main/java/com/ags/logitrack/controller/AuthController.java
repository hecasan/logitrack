package com.ags.logitrack.controller;

import com.ags.logitrack.dto.LoginRequestDTO;
import com.ags.logitrack.dto.LoginResponseDTO;
import com.ags.logitrack.dto.UsuarioRequestDTO;
import com.ags.logitrack.dto.UsuarioResponseDTO;
import com.ags.logitrack.repository.UsuarioRepository;
import com.ags.logitrack.service.JwtService;
import com.ags.logitrack.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticação
 * Gerencia endpoints de login e registro de usuários
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

   private final AuthenticationManager authenticationManager;
   private final UsuarioService usuarioService;
   private final JwtService jwtService;
   private final UsuarioRepository usuarioRepository;

   /**
    * Endpoint de login
    * Autentica usuário e retorna token JWT
    * 
    * @param loginRequest Credenciais do usuário
    * @return ResponseEntity<LoginResponseDTO> Token e dados do usuário
    */
   @PostMapping("/login")
   public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
      try {
         // Autentica o usuário
         Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                     loginRequest.getUsername(),
                     loginRequest.getPassword()));

         // Obtém os detalhes do usuário autenticado
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();

         // Gera o token JWT
         String token = jwtService.generateToken(userDetails);

         // Busca dados completos do usuário
         UsuarioResponseDTO usuario = usuarioService.buscarPorUsername(userDetails.getUsername());

         // Atualiza último acesso
         usuarioService.atualizarUltimoAcesso(userDetails.getUsername());

         // Retorna resposta com token e dados do usuário
         LoginResponseDTO response = new LoginResponseDTO(token, usuario);
         return ResponseEntity.ok(response);

      } catch (Exception e) {
         // Em caso de erro de autenticação
         throw new RuntimeException("Credenciais inválidas: " + e.getMessage());
      }
   }

   /**
    * Endpoint de registro de usuários
    * Permite que administradores cadastrem novos usuários
    * 
    * @param usuarioRequest Dados do novo usuário
    * @return ResponseEntity<UsuarioResponseDTO> Usuário criado
    */
   @PostMapping("/register")
   public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
      try {
         UsuarioResponseDTO usuarioCreated = usuarioService.criarUsuario(usuarioRequest);
         return ResponseEntity.ok(usuarioCreated);
      } catch (Exception e) {
         throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
      }
   }

   /**
    * Endpoint para validar token
    * Verifica se o token JWT ainda é válido
    * 
    * @param token Token a ser validado
    * @return ResponseEntity<Boolean> Status da validação
    */
   @PostMapping("/validate-token")
   public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
      try {
         String username = jwtService.extractUsername(token);
         UsuarioResponseDTO usuario = usuarioService.buscarPorUsername(username);

         // Token é válido se conseguiu extrair username e usuário existe
         return ResponseEntity.ok(usuario != null);
      } catch (Exception e) {
         return ResponseEntity.ok(false);
      }
   }

   /**
    * Endpoint para refresh do token
    * Gera um novo token para usuário autenticado
    * 
    * @param token Token atual
    * @return ResponseEntity<LoginResponseDTO> Novo token
    */
   @PostMapping("/refresh-token")
   public ResponseEntity<LoginResponseDTO> refreshToken(@RequestParam String token) {
      try {
         String username = jwtService.extractUsername(token);
         UsuarioResponseDTO usuario = usuarioService.buscarPorUsername(username);

         // Busca o usuário completo para gerar novo token
         UserDetails userDetails = usuarioRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
         String newToken = jwtService.generateToken(userDetails);

         LoginResponseDTO response = new LoginResponseDTO(newToken, usuario);
         return ResponseEntity.ok(response);
      } catch (Exception e) {
         throw new RuntimeException("Erro ao renovar token: " + e.getMessage());
      }
   }
}
