package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)/* @SpringBootTest indica que a Classe UsuariRepositoryTest é uma 
Classe Spring Boot Teste. A opção WebEnvirnment indica que caso a porta principal(8080 para uso local) esteja ocupada, o Spring irá atrbuir uma
outra porta automaticamente*/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)/*@TestInstance permite modificar o ciclo de vida da classe de testes.
Lifecycle.PER_CLASS(útilizado com as anotações @BeforeAll e @AfterAll) indica que o ciclo da Classe de Teste será por Classe*/
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/*O método start(), anotado com a anotação @BeforeAll, inicializa 5 objetos do tipo Usuario e insere no Banco de dados de teste através
	do método .save() uma única vez(Lifecycle.PER_CLASS)*/
	@BeforeAll
	void start() {
		
		//Apaga todos os registros do banco de dados antes de iniciar os testes
		 usuarioRepository.deleteAll();
		 
		
		usuarioRepository.save(new Usuario(0L, "DJ Cleiton Rasta", "cleitinho@pedra.com", "cabecadegelo", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		usuarioRepository.save(new Usuario(0L, "DJ Laurinha Lero", "laurinha@lero.com", "laura123", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		usuarioRepository.save(new Usuario(0L, "DJ Ednaldo Pereira", "ednaldo@pereira.com", "naovalenada", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		usuarioRepository.save(new Usuario(0L, "Naninha", "naninha@mc.com", "trabalholindo", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		usuarioRepository.save(new Usuario(0L, "Florentina", "flor@entina.com", "dejesus", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
	}
	
	@Test //O Método deveRetornarUmUsuario() foi anotado com a anotação @Test que indica que este método executará um teste.
	@DisplayName("Retornar um usuário")//A anotação @DisplayName configura uma mensagem que será exibida ao invés do nome do método
	public void deveRetornarUmUsuario() {
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("flor@entina.com");//o objeto usuario recebe o resultado do método findByUsuario()
		assertTrue(usuario.get().getUsuario().equals("flor@entina.com"));/*através do método de asserção assertTrue(), verifica se o usuário cujo email
		é “joao@email.com.br” foi encontrado*/
		
	}
	
	@Test
	@DisplayName("Retornar três usuários")
	public void deveRetornarTresUsuarios() {
		
		/* O objeto listaDeUsuarios recebe o resultado do método
		findAllByNomeContainingIgnoreCase()*/
		List<Usuario> listaDeUsuario = usuarioRepository.findAllByNomeContainingIgnoreCase("DJ");
		
		/*através do método de asserção assertEquals(), verifica se o tamanho da List é igual 
		a 3 (quantidade de usuário cadastrados no método start() cujo pré-fixo no nome é "DJ"). O método size(), (java.util.List), 
		retorna o tamanho da List. Se o tamanho da List for igual a 3, o 1º teste será aprovado*/
		assertEquals(3, listaDeUsuario.size());
		
		/*através do método de asserção AssertTrue(), verifica em cada posição da Collection List listaDeUsuarios se os usuários, que foram 
		  inseridos no Banco de dados através do método start(), foram gravados na mesma sequência.*/
		assertTrue(listaDeUsuario.get(0).getNome().equals("DJ Cleiton Rasta"));
		assertTrue(listaDeUsuario.get(1).getNome().equals("DJ Laurinha Lero"));
		assertTrue(listaDeUsuario.get(2).getNome().equals("DJ Ednaldo Pereira"));
		
		/*O Teste da linha 70 checará se o primeiro usuário inserido (DJ Cleiton Rasta) está na
		posição 0 da List listaDeUsuarios (1ª posição da List), e assim a verificação ocorrerá com os outros assertTrue e as posições.*/
		
		/*Posição na List é obtida através do método get(int index)(java.util.List), passando como parâmetro a posição desejada. 
		  O nome do usuário é obtido através do método getNome() da Classe Usuário. Se os três foram gravados na mesma sequência do 
		  método start(), os três testes serão Aprovados.*/
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}
	
}
