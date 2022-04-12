package org.generation.blogpessoal.repository;

import java.util.List;

import org.generation.blogpessoal.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Indica que ao Spring que a classe é um Repository
//Iremos herdar a interface Jparepository<T-tipo da model(mesma da model), ID-tipo do ID usado(Long)>
public interface PostagemRepository extends JpaRepository<Postagem, Long> { 
	
	// criando queryMethod para nossas consultas
	public List<Postagem> findAllByTituloContainingIgnoreCase (String titulo);
	
}
