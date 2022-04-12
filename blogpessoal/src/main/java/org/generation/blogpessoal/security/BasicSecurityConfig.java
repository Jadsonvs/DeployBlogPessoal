package org.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity //permite habilitar nossa configura de segurança e quais rotas bloquear ou não
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override //permitir um novo método de login do usuário(usuário em memória, para teste. Apgar para subit sistema)
	//alias
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(userDetailsService);
		
		auth.inMemoryAuthentication()
		.withUser("root")
		.password(passwordEncoder().encode("root"))
		.authorities("ROLE_USER");
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override 
	protected void configure(HttpSecurity http) throws Exception{ //Criar as rotas/endpoints que serão permitas ou não
		
		http.authorizeRequests()// autoriração de requisição
		.antMatchers("/usuarios/logar").permitAll()// codigo que escolhermos quais endpoints/caminhos iremos liberar
		.antMatchers("/usuarios/cadastrar").permitAll()
		.antMatchers(HttpMethod.OPTIONS).permitAll()
		.anyRequest().authenticated()// todas as outras requisições  precisamn ser autenticadas, terem a chave/token
		.and().httpBasic() // o padrão será o httpBasic, ou seja, utilizaremos o Basic para gerar o token
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Não possibilita o uso de mais de uma sessão/token
		.and().cors()// Autoriza a anotação CrossOrigin
		.and().csrf().disable(); //

	}
	
		
}
