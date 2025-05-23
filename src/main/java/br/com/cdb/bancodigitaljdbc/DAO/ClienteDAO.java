package br.com.cdb.bancodigitaljdbc.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.entity.Cliente;
import br.com.cdb.bancodigitaljdbc.rowMapper.ClienteRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ClienteDAO {

	private final JdbcTemplate jdbcTemplate;
	private final ClienteRowMapper clienteRowMapper;

	public void save(Cliente cliente) {

		String sql = "CALL inserir_cliente_v1 (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getCep(),
				cliente.getRua(), cliente.getBairro(), cliente.getCidade(), cliente.getEstado(),
				cliente.getTipoDeCliente().toUpperCase());
	}

	public Cliente update(Cliente cliente) {
		String sql = """
				UPDATE cliente SET nome = ?, cpf = ?, data_nascimento  = ?, cep = ?,
				rua = ?, bairro = ?, cidade = ?, estado = ?, tipo_de_cliente  = ?
				 WHERE id = ?
				 """;
		jdbcTemplate.update(sql, cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(), cliente.getCep(),
				cliente.getRua(), cliente.getBairro(), cliente.getCidade(), cliente.getEstado(),
				cliente.getTipoDeCliente().toUpperCase(), cliente.getId());
		return cliente;
	}

	public void delete(Long id) {
		String sql = "DELETE FROM cliente WHERE id = ?";
		jdbcTemplate.update(sql, id);
		// update pode ser usado pro que não me retorna um valor e sim faz uma ação
		// ex: insert, delete, update, alter table.
	}

	public List<Cliente> findAll() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

	  public Optional<Cliente> findById(Long id) {
	        String sql = "SELECT * FROM cliente WHERE id = ?";
	        List<Cliente> lista = jdbcTemplate.query(sql, clienteRowMapper, id);
	        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
	    }

	public Optional<Cliente> findByCPF(String cpf) {
		String sql = "SELECT * FROM cliente WHERE cpf = ?";
		List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, cpf);
		return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
		// se estiver vazia, retorna q tá... se não, retorna o primeiro pegando o index
	}

}
