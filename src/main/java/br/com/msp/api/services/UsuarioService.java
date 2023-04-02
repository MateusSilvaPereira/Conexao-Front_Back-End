package br.com.msp.api.services;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.msp.api.dto.UsuarioDTO;
import br.com.msp.api.model.Usuario;
import br.com.msp.api.repository.IUsuario;
import br.com.msp.api.security.Token;
import br.com.msp.api.security.TokenUtil;
@SuppressWarnings("deprecation")
@Service
public class UsuarioService {

	private final IUsuario repository;

	private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	private final PasswordEncoder passwordEncoder;
	
	// Injeção de dependencia
	public UsuarioService (IUsuario repository) {
		this.repository = repository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
	}

	public List<Usuario> listaUsuario(){
		logger.info("Usuario: " + getUserLogado() + " Listando Usuarios");
		return repository.findAll();
	}
	
	public Usuario criarUsuario(Usuario usuario) {
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);

		logger.info("Usuario: " + getUserLogado() + " Cadastrando Usuarios");
		return repository.save(usuario);
	}
	
	public Usuario atualizarUsuario(Usuario usuario) {
		
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);

		logger.info("Usuario: " + getUserLogado() + " Atualizando Usuario: " + usuario);
		return repository.save(usuario);
	}
	
	public void deletarUsuario(Integer id) {
		logger.info("Usuario: " + getUserLogado() + " Excluindo Usuario");
		repository.deleteById(id);
		
	}
	
	public Boolean validarUsuario(Usuario usuario) {
		String senha = repository.getById(usuario.getId()).getSenha();
		Boolean valid = passwordEncoder.matches(usuario.getSenha(), senha);
		return valid;
	}

	public Token gerarToken(@Valid UsuarioDTO usuario) {

		Usuario user = repository.findByNomeOrEmail(usuario.getNome(), usuario.getEmail());
		if(user != null){
			 Boolean valid = passwordEncoder.matches(usuario.getSenha(), user.getSenha());
				if(valid) {
					return new Token(TokenUtil.createToken(user));
				}
		}
		return null;
	}

	private String getUserLogado(){
		Authentication userLogado = SecurityContextHolder.getContext().getAuthentication();
		if(!(userLogado instanceof AnonymousAuthenticationToken)){
			return userLogado.getName();
		}
		return  "Null";
	}
	
}
