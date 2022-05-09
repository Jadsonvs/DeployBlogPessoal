package org.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.model.UsuarioLogin;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//método cadastrarUsuario

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(usuarioRepository.save(usuario));		
	}
	
	// método autenticarUsuario --> que será usado no logar 
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha()));// pegar senha encriptada e não criptografada e compara se são iguais, se iguais retorna verdadeiro
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setTipo(usuario.get().getTipo());
				usuarioLogin.get().setToken(geradorBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
		}
		
		return Optional.empty();
	}
	   
	private boolean compararSenhas(String senhaDigitada, String senhaDoBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaDoBanco);
	}
	
	// -------- tentando criar Método atualizar --------
	
		public Optional<Usuario> atualizarUsuario(Usuario usuario) {
			if(usuarioRepository.findById(usuario.getId()).isPresent()) {
				
				Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
				
				if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

				 usuario.setSenha(criptografarSenha(usuario.getSenha()));

				return Optional.ofNullable(usuarioRepository.save(usuario));
				
			}
			
			return Optional.empty();
		}
	
	//-------Criptografar senha ----------
	
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //recebe a senha e criptografa a senha
		
		return encoder.encode(senha);
	}
	// ------ Gerar token ------
	private String geradorBasicToken(String usuario, String senha) {
		
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
				return "Basic " + new String(tokenBase64);
		
	}
	
	
}
