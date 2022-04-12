package org.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.generation.blogpessoal.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {// implementar a regra de negocio UserDetails que já existe
	
	private static final long serialVersionUID = 1L;// 
	
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities; //autorização de administrador
	
	public UserDetailsImpl(Usuario usuario) { //Construtor de classe cheio
		userName = usuario.getUsuario();
		password = usuario.getSenha();
		

	}
	
	public UserDetailsImpl () {} //Construtor vazio para teste
	
	@Override //Para que o basic security pegue o getPassword e return o password para acessar o sistema
	public String getPassword() {
		return password;
	}
	
	@Override  //Para que o basic security pegue o getUsername e return o userName para acessar o sistema
	public String getUsername() {
		return userName;
	}
	
	
	
	//Métodos padrões basic security. Colocamos todos como padrão para o basic security não perguntar
	@Override //autorização de administrador
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
		public boolean isAccountNonLocked() {
			return true;
		}
	@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}
	@Override
		public boolean isEnabled() {
			return true;
		}
	
	


}
