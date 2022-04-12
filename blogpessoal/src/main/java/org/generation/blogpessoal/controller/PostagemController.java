package org.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.generation.blogpessoal.model.Postagem;
import org.generation.blogpessoal.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Informar ao Spring que essa é uma classe Repository
@RequestMapping("/postagens") //Informaar por qual uri essa classe será acessada
@CrossOrigin(origins="*") //Permitir que nossa classe aceite qualquer tipo de requisição
public class PostagemController {

	@Autowired //Injeção de dependência/ todos os serviços do tipo postagem repository sejam acessados a partir do Controller
	public PostagemRepository repository; // injetar nossa classe Repository dentro da nossa classe Controller
	
	@GetMapping /*Expor para nossa API que esse método trata-se de um GET. Caso venha requisição para o a uri /postagens
	 e o método dessa requisição for o método GET, ele vai disparar o método findAll*/
	public ResponseEntity<List<Postagem>> getAll() { //método para requisição de todas as postagens. Método findAll
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")//Método HTTP com tipo de requisição id definido entre {}
	public ResponseEntity<Postagem> getById (@PathVariable long id) { //método irá captar a variável id, recepcionando o atributo id pelo método @PathVariable
		return repository.findById(id) //retorna a interface repository com o método findById
				.map(resp -> ResponseEntity.ok(resp)) //O método findById pode devolver um objeto do tipo Postagem com ok e o objeto dentro do corpo da requisição
				.orElse(ResponseEntity.notFound().build());//ou um objeto notFound caso não exista o objeto ou tenha algum erro
	}
	
	@GetMapping("/titulo/{titulo}")//utilizando subcaminho /titulo com atributo {titulo}(onde colocamos o titulo que vamos pesquisar para não confundir o backend nas rotas passadas
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){//método getByTitulo que vai recepcionar a rota com atributo titulo, método do tipo lista
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));//irá retorna um objeto 'OK' e buscar tudo que contenha o valor titulo no banco de dados, ignorando letra maiuscula ou minuscula
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem){//método que vai inserir/criar algum dado onde esse dado será pego pelo body(corpo da requisição). Posso usar postPostagem ou apenas post
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(repository.save(postagem));//Irá retornar o status CREATED e salvo a nossa requisição post
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put (@RequestBody Postagem postagem){// Método que irá atualizar algum dado no banco de dados
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));//Irá retornar o status ok e salvo a nossa requisição para a atualização PUT
	}
	
	@DeleteMapping("/{id}")//informar que é um método com um caminho e com o seu parâmetro(atributo)
	public void delete(@PathVariable long id) {// criando o método do tipo que não retorna nada e irá receber como parâmetro o id do tipo long
		repository.deleteById(id);// chamar a interface repository e usar o método deleteById, passando o id como valor
	}
	
	
	
	
	
	
	
	
	
}
