package com.ags.logitrack.repository;

import com.ags.logitrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de banco de dados relacionadas aos usuários
 * Extends JpaRepository para herdar métodos básicos de CRUD
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   /**
    * Busca um usuário pelo nome de usuário
    * 
    * @param username Nome de usuário
    * @return Optional<Usuario> usuário encontrado ou vazio
    */
   Optional<Usuario> findByUsername(String username);

   /**
    * Busca um usuário pelo email
    * 
    * @param email Email do usuário
    * @return Optional<Usuario> usuário encontrado ou vazio
    */
   Optional<Usuario> findByEmail(String email);

   /**
    * Verifica se existe um usuário com o nome de usuário fornecido
    * 
    * @param username Nome de usuário
    * @return boolean true se existir, false caso contrário
    */
   boolean existsByUsername(String username);

   /**
    * Verifica se existe um usuário com o email fornecido
    * 
    * @param email Email do usuário
    * @return boolean true se existir, false caso contrário
    */
   boolean existsByEmail(String email);

   /**
    * Busca todos os usuários ativos
    * 
    * @return List<Usuario> lista de usuários ativos
    */
   List<Usuario> findByAtivoTrue();

   /**
    * Busca usuários por tipo
    * 
    * @param tipoUsuario Tipo do usuário (ADMIN ou USUARIO)
    * @return List<Usuario> lista de usuários do tipo especificado
    */
   List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

   /**
    * Busca usuários ativos por tipo
    * 
    * @param tipoUsuario Tipo do usuário
    * @return List<Usuario> lista de usuários ativos do tipo especificado
    */
   List<Usuario> findByTipoUsuarioAndAtivoTrue(Usuario.TipoUsuario tipoUsuario);

   /**
    * Busca usuários por nome completo (busca parcial, case insensitive)
    * 
    * @param nomeCompleto Nome a ser buscado
    * @return List<Usuario> lista de usuários que contêm o nome
    */
   @Query("SELECT u FROM Usuario u WHERE LOWER(u.nomeCompleto) LIKE LOWER(CONCAT('%', :nome, '%')) AND u.ativo = true")
   List<Usuario> findByNomeCompletoContainingIgnoreCase(@Param("nome") String nomeCompleto);

   /**
    * Conta quantos administradores existem no sistema
    * 
    * @return long número de administradores
    */
   @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = 'ADMIN' AND u.ativo = true")
   long countAdministradores();
}
