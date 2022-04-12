package org.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/*A Anotação (Annotation)@Configuration indica que a Classe é do tipo configuração, ou seja, define uma classe como fonte de 
 * definições de beans e é uma das anotações essenciais se você estiver usando a configuração baseada em Java.*/
@Configuration
public class SwaggerConfig {
	
	/*A Anotação @Bean utilizada em métodos de uma classe, indica que este objeto
	poder ser injetado em qualquer ponto da sua aplicação.*/
	@Bean
	public OpenAPI springBlogPessoalOpenAPI() {
		
		return new OpenAPI()//Cria um Objeto da Classe OpenAPI, que gera a documentação no Swagger utilizando a especificação OpenAPI.
				//Linhas 27 a 30:Insere as informações sobre a API (Nome do projeto (Title), Descrição e Versão).
				  	.info(new Info()
						.title("Projeto Blog Pessoal")
						.description("Projeto Blog Pessoal - Generation Brasil")
						.version("v0.0.1")
				//Linhas 32 a 34:Insere as informações referentes a licença da API (Nome e Link).
					.license(new License()
						.name("Generation Brasil")
						.url("https://brazil.generation.org/"))
				//Linhas 36 a 39:Insere as informações de contato da pessoa Desenvolvedora (Nome, Site e E-mail).
					.contact(new Contact()
						.name("Conteudo Generation")
						.url("https://github.com/conteudoGeneration")
						.email("conteudogeneration@gmail.com")))
				//Linhas 41 a 43:Insere as informações referentes a Documentações Externas (Github,Gitpage e etc), onde são informados o Nome e o Link.
					.externalDocs(new ExternalDocumentation()
						.description("Github")
						.url("https://github.com/conteudoGeneration/"));		
		
	}
	
	/*A Classe OpenApiCustomiser permite personalizar o Swagger, baseado na 
     Especificação OpenAPI. O Método abaixo, personaliza todas as mensagens 
     HTTP Responses (Respostas das requisições) do Swagger.*/
	
	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {

		//Cria um Objeto da Classe OpenAPI, que gera a documentação no Swagger utilizando a especificação OpenAPI.
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
				
//Cria um Objeto da Classe ApiResponses, que receberá as Respostas HTTP de cada endpoint (Paths) através do método getResponses().
				ApiResponses apiResponses = operation.getResponses();

/*Adiciona as novas Respostas no endpoint, substituindo as atuais e acrescentando as demais, através do Método addApiResponse(), 
 * identificadas pelo HTTP Status Code (200, 201 e etc).*/
				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

			}));
		};
	}

    //O Método createApiResponse() adiciona uma descrição (Mensagem), em cada Resposta HTTP.
	private ApiResponse createApiResponse(String message) {

		return new ApiResponse().description(message);

	}
	
}
