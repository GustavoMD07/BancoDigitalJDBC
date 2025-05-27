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
		String sql = "SELECT atualizar_cliente_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.queryForObject(sql, Boolean.class, cliente.getId(), cliente.getNome(), cliente.getCpf(),
				cliente.getDataNascimento(), cliente.getCep(),
				cliente.getRua(), cliente.getBairro(), cliente.getCidade(),
				cliente.getEstado(), cliente.getTipoDeCliente().toUpperCase());
		return cliente;
	} //toda função que tiver um "return" mesmo que void, não pode ser update
	//o update não recebe retorno, select + update não casam bem
	

	public void delete(Long id) {
		String sql = "SELECT deletar_cliente_v2(?)";
		jdbcTemplate.queryForObject(sql, Boolean.class, id);
		// update pode ser usado pro que não me retorna um valor e sim faz uma ação
		// ex: insert, delete, update, alter table.
	}

	public List<Cliente> findAll() {
        String sql = "SELECT * FROM encontrar_todos_cliente_v1()";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

	  public Optional<Cliente> findById(Long id) {
	        String sql = "SELECT * FROM encontrar_cliente_v1(?)";
	        List<Cliente> lista = jdbcTemplate.query(sql, clienteRowMapper, id);
	        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
	    }

	public Optional<Cliente> findByCPF(String cpf) {
		String sql = "SELECT * FROM encontrar_cliente_cpf_v1(?)";
		List<Cliente> clientes = jdbcTemplate.query(sql, clienteRowMapper, cpf);
		return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
		// se estiver vazia, retorna q tá... se não, retorna o primeiro pegando o index
	}

}
