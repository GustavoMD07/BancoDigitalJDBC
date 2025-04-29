package br.com.cdb.bancodigitalJPA.security.repository;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitalJPA.security.model.Usuario;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsuarioDAO {
	
	private final JdbcTemplate jdbcTemplate;
	

	public Usuario save(Usuario usuario) {
		
		String SQL = "INSERT INTO usuario (nome, emaill, senha, role) "
		+ "VALUES (?,?,?, ?)";
		
		jdbcTemplate.update(SQL, usuario.getNome(), usuario.getEmail(),
			usuario.getSenha(), usuario.getRole());
		
		return usuario;
	}
	
	public Optional<Usuario> findByEmail(String email) {
		String sql ="SELECT * FROM usuario WHERE email = ?";
		jdbc.query(sql,)
		return Optional.of(usuario);
	}
}
