package br.com.msp.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.msp.api.dto.UsuarioDTO;
import br.com.msp.api.model.Usuario;
import br.com.msp.api.security.Token;
import br.com.msp.api.services.UsuarioService;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuarioController{
	
	private UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> listausuarios () {
		
		List<Usuario> listUser = usuarioService.listaUsuario();
		return ResponseEntity.ok().body(listUser);
	}
	
	@PostMapping
	public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) {
		Usuario usuarioNovo = usuarioService.criarUsuario(usuario);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNovo);
	}
	
	@PutMapping
	public ResponseEntity<Usuario> editarUsuario(@Valid @RequestBody Usuario usuario){
		Usuario usuarioNovo = usuarioService.atualizarUsuario(usuario);
		return ResponseEntity.ok().body(usuarioNovo);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> ecluirUsuario(@PathVariable Integer id){
		usuarioService.deletarUsuario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<Token> validarSenha(@Valid @RequestBody UsuarioDTO usuario){

		Token token = usuarioService.gerarToken(usuario);
		if(token != null) {
			return ResponseEntity.status(HttpStatus.OK).body(token);
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	                     
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){
		
		Map<String, String> errors =  new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fielName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			
			errors.put(fielName, errorMessage);
		});
		return errors;
	}
	
}
