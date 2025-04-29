package br.com.cdb.bancodigitalJPA.DAO;


import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ClienteDAO {

	private final JdbcTemplate jdbcTemplate;
	
	public void save(Cliente cliente) {
		
	}
	
	public List<Cliente> findAll() {
		List<Cliente> clientes = new ArrayList<>();
		return clientes;
	}
	
	public Cliente findById(Long id) {
		Cliente cliente = null;
		return cliente;
	}
	
	public void update(Cliente cliente) {

	}
	
	public void delete(Long id) {

	}

}
