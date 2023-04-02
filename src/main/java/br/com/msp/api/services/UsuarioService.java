package br.com.msp.api.services;

import java.util.List;

import javax.validation.Valid;

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

	private IUsuario repository;
	
	private PasswordEncoder passwordEncoder;
	
	// Injeção de dependencia
	public UsuarioService (IUsuario repository) {
		this.repository = repository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
	}
	
	public List<Usuario> listaUsuario(){
		List<Usuario> listUser = repository.findAll();
		return listUser;
	}
	
	public Usuario criarUsuario(Usuario usuario) {
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);
		Usuario user = repository.save(usuario);
		return user;
	}
	
	public Usuario atualizarUsuario(Usuario usuario) {
		
		String encoder = this.passwordEncoder.encode(usuario.getSenha());
		usuario.setSenha(encoder);
		Usuario userUpdate = repository.save(usuario);
		return userUpdate;
	}
	
	public void deletarUsuario(Integer id) {
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
	
}
