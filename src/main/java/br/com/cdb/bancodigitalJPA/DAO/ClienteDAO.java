package br.com.cdb.bancodigitalJPA.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.rowMapper.ClienteRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ClienteDAO {

	private final JdbcTemplate jdbcTemplate;
	
	public Cliente save(Cliente cliente) {
		
		String sql = "INSERT INTO cliente (nome, cpf, data_nascimento, cep) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getCep());
		return cliente;
	}
		
	public void update(Cliente cliente) {
		
		String sql = "UPDATE Cliente SET nome = ?, cpf = ?, "
		+ "data_nascimento = ?, cep = ? WHERE id = ?";
		jdbcTemplate.update(sql, cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), 
		cliente.getCep(), cliente.getId());
	}
	
	public void delete(Long id) {
		String sql = "DELETE INTO cliente WHERE id = ?";
		jdbcTemplate.update(sql, id);
		//update pode ser usado pro que não me retorna um valor e sim faz uma ação
		//ex: insert, delete, update, alter table.
	}
	
	public List<Cliente> findAll() {
		String sql = "SELECT * FROM cliente";
		return jdbcTemplate.query(sql, new ClienteRowMapper());
	}
	
	public Optional<Cliente> findById(Long id) {
		String sql = "SELECT * FROM Cliente WHERE id = ?";
		List<Cliente> clientes = jdbcTemplate.query(sql, new ClienteRowMapper(), id);
		return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
	}
	
	public Optional<Cliente> findByCPF(String cpf) {
		String sql = "SELECT * FROM Cliente WHERE cpf = ?";
		List<Cliente> clientes = jdbcTemplate.query(sql, new ClienteRowMapper(), cpf);
		return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
		//se estiver vazia, retorna q tá... se não, retorna o primeiro pegando o index
	}

}
