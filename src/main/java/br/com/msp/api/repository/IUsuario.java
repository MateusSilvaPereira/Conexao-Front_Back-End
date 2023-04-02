package br.com.msp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.msp.api.model.Usuario;

@Repository
public interface IUsuario extends JpaRepository<Usuario, Integer>{

	public Usuario findByNomeOrEmail(String nome, String email);

}
