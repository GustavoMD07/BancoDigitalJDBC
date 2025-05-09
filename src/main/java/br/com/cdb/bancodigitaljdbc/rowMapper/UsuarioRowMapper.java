package br.com.cdb.bancodigitaljdbc.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import br.com.cdb.bancodigitaljdbc.security.model.Role;
import br.com.cdb.bancodigitaljdbc.security.model.Usuario;

public class UsuarioRowMapper implements RowMapper<Usuario> {

	//essa classe serve basicamente como um tradutor de SQL -> Java
	//ele lê as linhas do database e transforma em objeto java, assim
	//com ele é possível retornar um findAll ou findById.
	//sempre algo que eu precise retornar
	//linha de dados = ResultSet
	
	@Override
	public Usuario mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
		Usuario usuario = new Usuario();
		usuario.setId(rs.getLong("id"));
		usuario.setNome(rs.getString("nome"));
		usuario.setEmail(rs.getString("email"));
		usuario.setSenha(rs.getString("senha"));
		usuario.setRole(Role.valueOf(rs.getString("role")));
		return usuario;
	}

	
}
