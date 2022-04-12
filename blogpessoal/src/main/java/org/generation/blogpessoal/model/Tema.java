package org.generation.blogpessoal.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name= "tb_tema")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String descricao;
	
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)/*Criando e definindo tipo de ligação que será feita na foreign key. 
	Um tema para várias postagens. Mapeando o atributo tema da tabela postagem. cascade tipo ALL fará que quando atualizarmos ou 
	deletarmos o tema, todas as postagens relacionadas nesse tema sofra essas alterações*/
	@JsonIgnoreProperties("tema")/*anotação para não criar recursividade. Parârametro que será ignorado na classe Postagem será tema
    ou seja quando chegar em tema irá parar de apresentar informações*/ 
	private List<Postagem> postagem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	
}
