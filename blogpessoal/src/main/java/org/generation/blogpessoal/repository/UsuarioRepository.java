package org.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	//---método query criado para verificar se usuário existe no banco de dados---
	public Optional<Usuario> findByUsuario(String usuario);
	
	//---método query criado para teste no JUnit5---
	public List<Usuario> findAllByNomeContainingIgnoreCase(String nome);	
}
