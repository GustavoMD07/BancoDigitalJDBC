package br.com.cdb.bancodigitalJPA.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.cdb.bancodigitalJPA.database.DatabaseConnection;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.ClienteComum;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.ClienteSuper;

public class ClienteDAO {

	public void save(Cliente cliente) throws SQLException {
		
		Connection connect = DatabaseConnection.getConnection(); 
		//responsável por conectar o Java com o bd
		
		String sql = "insert into clientes (id, nome, cpf, data_nascimento, "
			+ "cep, tipo_de_cliente, estado, cidade, bairro, rua) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		// ? = placeholders
		
		PreparedStatement ps = connect.prepareStatement(sql);
		ps.setLong(1, cliente.getId());
		ps.setString(2, cliente.getNome());
		ps.setString(3, cliente.getCpf());
		ps.setDate(4, java.sql.Date.valueOf(cliente.getDataNascimento()));
		ps.setString(5, cliente.getCep());
		ps.setString(6, cliente.getTipoDeCliente());
		ps.setString(7, cliente.getEstado());
		ps.setString(8, cliente.getCidade());
		ps.setString(9, cliente.getBairro());
		ps.setString(10, cliente.getRua());
		ps.executeUpdate();
		ps.close();
		connect.close(); //fecha as conexões com o banco 
	}
	
	public List<Cliente> findAll() throws SQLException {
		Connection connect = DatabaseConnection.getConnection();
		String sql = "select * from clientes";
		PreparedStatement ps = connect.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery(); //o ResultSet lê as linhas do bd, uma por uma
		
		List<Cliente> clientes = new ArrayList<>();
		
		while(rs.next()) {
			String tipoCliente = rs.getString("tipo_cliente");

	        Cliente cliente = null;

	        if ("comum".equalsIgnoreCase(tipoCliente)) {
	            cliente = new ClienteComum();
	        } else if ("super".equalsIgnoreCase(tipoCliente)) {
	            cliente = new ClienteSuper();
	        } else if ("premium".equalsIgnoreCase(tipoCliente)) {
	            cliente = new ClientePremium();
	        } else {
	            throw new RuntimeException("Tipo de cliente desconhecido: " + tipoCliente);
	        }
			cliente.setId(rs.getLong("id"));
			cliente.setNome(rs.getString("nome"));
			cliente.setCpf(rs.getString("cpf"));
			cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
			cliente.setCep(rs.getString("cep"));
			cliente.setTipoDeCliente(rs.getString("tipo_de_cliente"));
			cliente.setEstado(rs.getString("estado"));
			cliente.setCidade(rs.getString("cidade"));
			cliente.setBairro(rs.getString("bairro"));
			cliente.setRua(rs.getString("rua"));

			clientes.add(cliente);
		}
		rs.close();
		ps.close();
		connect.close();
		return clientes;
	}

}
