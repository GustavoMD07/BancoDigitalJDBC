package br.com.cdb.bancodigitaljdbc.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.rowMapper.UsuarioRowMapper;
import br.com.cdb.bancodigitaljdbc.security.model.Usuario;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsuarioDAO {
	
	private final JdbcTemplate jdbcTemplate;
	

	public Usuario save(Usuario usuario) {
		
		String SQL = "INSERT INTO usuario (nome, email, senha, role) "
		+ "VALUES (?,?,?, ?)";
		
		jdbcTemplate.update(SQL, usuario.getNome(), usuario.getEmail(),
			usuario.getSenha(), usuario.getRole().name());
		
		return usuario;
	}
	//query é só um comando SQL
	public Optional<Usuario> findByEmail(String email) {
		String sql ="SELECT * FROM usuario WHERE email = ?";
		List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), email);		
		return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
		//se a lista estiver vazia, retorna que tá vazio, se tiver, ele pega o primeiro pelo index
	}
}
