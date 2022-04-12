package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/*A anotação @TestMethodOrder indica em qual ordem os testes serão executados. A opção MethodOrderer.OrderAnnota.class indica que os testes
  serão executados na ordem indicada pela anotação @Order inserida em cada teste.*/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	/*Foi injetado (@Autowired), um objeto da Classe UsuarioService para persistir os objetos no Banco de dados de testes com
	  a senha criptografada*/
	@Autowired
	private UsuarioService usuarioService;
	
	/*Foi injetado (@Autowired), um objeto da Classe TestRestTemplate para enviar as requisições para a nossa aplicação. Somente no teste de 
	  controlller, pois usamos o padrão Rest que utiliza os verbos e métodos HTTP -> GET-POST-PUT-DELETE*/
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start(){

		 //Apaga todos os registros do banco de dados antes de iniciar os testes
		   usuarioRepository.deleteAll();
	}   
	
	@Test
	@Order(1)//indica que o método/teste será o primeiro a ser executado.
	@DisplayName("Cadastrar apenas um usuário")
	public void deveCadastrarUmUsuario() {
		
		/*Foi criado um objeto da Classe HttpEntity chamado reqquisição, recebendo um objeto da Classe Usuário. Nesta etapa, o processo é 
		   equivalente ao que o Postman faz em uma requisição do tipo POST: Transforma os atributos num objeto da Classe Usuário, que será 
		   enviado no corpo da requisição(Request Body).*/
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Jose","jose@imperiobronze.com","trabalholindo",
				"https://i.imgur.com/FETvs2O.jpg"));
		
		/*A Requisição HTTP será enviada através do método exchange() da Classe
		TestRestTemplate e a Resposta da Requisição (Response) será recebida pelo objeto
		resposta do tipo ResponseEntity. Para enviar a requisição, será necessário 4 parâmetros:

		A URI: Endereço do endpoint (/usuarios/cadastrar);
		O Método HTTP: Neste exemplo o método POST;
		O Objeto HttpEntity: Neste exemplo o objeto requisição, que contém o objeto da Classe Usuario;
		O conteúdo esperado no Corpo da Resposta (Response Body): Neste exemplo será do tipo Usuario(Usuario.class).*/
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		/*Através do método de asserção AssertEquals(), checaremos se a resposta
		da requisição (Response), é a resposta esperada (CREATED 🡪 201). Para obter o status
		da resposta vamos utilizar o método getStatusCode() da Classe ResponseEntity.*/
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		
		/*Através do método de asserção AssertEquals(), checaremos se o
		nome e o usuário(e-mail) enviados na requisição foram persistidos no Banco de dados.
		Através do método getBody() faremos o acesso aos objetos requisição e resposta, e
		através dos métodos getNome() e getUsuario() faremos o acesso aos atributos que serão
		comparados.*/
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		
	}


	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() {
		
		/*Através do método cadastrarUsuario() da Classe UsuarioService, foi
		persistido um Objeto da Classe Usuario no banco de dados().*/
		usuarioService.cadastrarUsuario(new Usuario(0L, "DJ Laurinha Lero", "laurinha@lero.com", "laura123", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		/*Foi criado um objeto HttpEntity chamado requisicao, recebendo um objeto
		da Classe Usuario contendo os mesmos dados do objeto persistido na linha 84("DJ Laurinha Lero").*/
		HttpEntity<Usuario> requisicao =  new HttpEntity<Usuario>(new Usuario(0L, "DJ Laurinha Lero", "laurinha@lero.com", "laura123", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		/*Através do método de asserção AssertEquals(), checaremos se a resposta
		da requisição (Response), é a resposta esperada (BAD_REQUEST 🡪 400). Para obter o
		status da resposta vamos utilizar o método getStatusCode() da Classe ResponseEntity.*/
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um usuário")
	public void deveAtualizarUsuario() {
		
		/*Foi criado um Objeto Optional, do tipo Usuario, chamado usuarioCreate, para armazenar o resultado da persistência de um
		  Objeto Usuario no Banco de dados, através do método cadastrarUsuario() da Classe UsuarioService.*/
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, "Lorena Borges", "lorena@gata.com", "jadson123", "https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		/*Foi criado um Objeto do Usuario, chamado usuarioUpdate, que será utilizado para atualizar
		os dados persistidos no Objeto usuarioCreate(linha 108 ).*/
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), "Lorena Borges dos Santos", "lorena_borges@gata.com", "jadson123", "https://i.imgur.com/FETvs2O.jpg\r\n");
		
		/*Foi criado um objeto da Classe HttpEntity chamado requisição, recebendo o objeto da Classe Usuário chamado usuarioUpdate. 
		   Nesta etapa, o processo é  equivalente ao que o Postman faz em uma requisição do tipo PUT: Transforma os atributos num 
		   objeto da Classe Usuário, que será enviado no corpo da requisição(Request Body).*/
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		/*Mudará apenas esses dois parâmetros para o envio da requisição
		A URI: Endereço do endpoint (/usuarios/cadastrar);
		O Método HTTP: Neste exemplo o método PUT;*/
		ResponseEntity<Usuario> resposta = testRestTemplate
				/*Observe que na linha 129, como o Blog Pessoal está com o Spring Security habilitado
				com autenticação do tipo Http Basic, o Objeto testRestTemplate dos endpoints que
				exigem autenticação, deverá efetuar o login com um usuário e uma senha válida para
				realizar os testes. Para autenticar o usuário e a senha utilizaremos o método withBasicAuth(user, password)
				da Classe TestRestTemplate. Como criamos o usuário em memória(root), na Classe BasicSecurityConfig, vamos
				usá-lo para autenticar o nosso teste.*/
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
			assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
			assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosUsuarios() {
		
		/*Na linhas 144 e 147, foram persistidos dois Objetos da Classe Usuario no Banco de
		dados, através do método cadastrarUsuario() da Classe UsuarioService.*/
		usuarioService.cadastrarUsuario(new Usuario(0L, "Jadson Viana", "jadson@lindo.com", "jadson123", 
				"https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Adriana Viana", "adriana@mae.com", "adriana123", 
				"https://i.imgur.com/FETvs2O.jpg\r\n"));
		
		/*Mudará apenas esses dois parâmetros para o envio da requisição
		A URI: Endereço do endpoint (/usuarios/all);
		O Método HTTP: Neste exemplo o método GET;
		O Objeto HttpEntity: O objeto será nulo (null). Requisições do tipo GET não enviam Objeto no corpo da requisição
		O conteúdo esperado no Corpo da Resposta (Response Body): Neste exemplo como o objeto da requisição é nulo, 
		  a resposta esperada será do tipo String(String.class) */
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}

	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}
}
