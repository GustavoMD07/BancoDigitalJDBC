package br.com.cdb.bancodigitaljdbc.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigitaljdbc.entity.Cliente;
import br.com.cdb.bancodigitaljdbc.entity.ClienteComum;
import br.com.cdb.bancodigitaljdbc.entity.ClientePremium;
import br.com.cdb.bancodigitaljdbc.entity.ClienteSuper;
import br.com.cdb.bancodigitaljdbc.exception.SubClasseDiferenteException;

@Component
public class ClienteRowMapper implements RowMapper<Cliente> {

	@Override
	public Cliente mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {

		String tipo = rs.getString("tipo_de_cliente");

		Cliente cliente;
		switch (tipo.toUpperCase()) {
		case "COMUM":
			cliente = new ClienteComum();
			break;
		case "SUPER":
			cliente = new ClienteSuper();
			break;
		case "PREMIUM":
			cliente = new ClientePremium();
			break;
		default:
			throw new SubClasseDiferenteException("Tipo de cliente inválido: " + tipo);
		}
		cliente.setId(rs.getLong("id"));
		cliente.setNome(rs.getString("nome"));
		cliente.setCpf(rs.getString("cpf"));
		cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
		cliente.setCep(rs.getString("cep"));
		cliente.setRua(rs.getString("rua"));
		cliente.setBairro(rs.getString("bairro"));
		cliente.setCidade(rs.getString("cidade"));
		cliente.setEstado(rs.getString("estado"));

		return cliente;

	}

}
