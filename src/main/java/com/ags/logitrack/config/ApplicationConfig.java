package com.ags.logitrack.config;

import com.ags.logitrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração da aplicação
 * Define beans necessários para autenticação e segurança
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

   private final UsuarioRepository usuarioRepository;

   /**
    * Bean para carregamento de detalhes do usuário
    * Usado pelo Spring Security para autenticação
    */
   @Bean
   public UserDetailsService userDetailsService() {
      return username -> usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
   }

   /**
    * Bean para provedor de autenticação
    * Configura como as credenciais são validadas
    */
   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService());
      authProvider.setPasswordEncoder(passwordEncoder());
      return authProvider;
   }

   /**
    * Bean para gerenciamento de autenticação
    * Usado para autenticar usuários programaticamente
    */
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
   }

   /**
    * Bean para codificação de senhas
    * Usa BCrypt para hash das senhas
    */
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
