package br.com.cdb.bancodigitalJPA.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.rowMapper.ContaRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ContaDAO {
	private final JdbcTemplate jdbcTemplate;
	
	public Conta save(Conta conta) {
		String sql = "INSERT INTO conta(cliente_id, tipo_de_conta) VALUES (?, ?)";
		jdbcTemplate.update(sql, conta.getCliente().getId(), conta.getTipoDeConta());
		return conta;
	}
	
	public void delete(Long id) {
		String sql = "DELETE FROM conta WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	public void update(Conta conta) {
		String sql = "UPDATE conta SET cliente_id = ?, tipo_de_conta = ? WHERE id = ?";
		jdbcTemplate.update(sql, conta.getCliente().getId(), conta.getTipoDeConta(), conta.getId());
		
	}
	
	public Optional<Conta> findById(Long id) {
		String sql = "SELECT * FROM conta WHERE ID  ?";
		List<Conta> contas = jdbcTemplate.query(sql,new ContaRowMapper(), id);
		return contas.isEmpty() ? Optional.empty() : Optional.of(contas.get(0));
	}
	
	public List<Conta> findAll() {
		String sql = "SELECT * FROM conta";
		return jdbcTemplate.query(sql, new ContaRowMapper());
	}
	
	public void deleteAll(List<Conta> contas) {
		String sql = "DELETE FROM conta WHERE id = ?";
		
		for (Conta conta : contas) {
			jdbcTemplate.update(sql, conta.getId());
		}
	}
	
	public List<Conta> findByClienteId(Long clienteId) {
	    String sql = "SELECT * FROM conta WHERE cliente_id = ?";
	    return jdbcTemplate.query(sql, new ContaRowMapper(), clienteId);
	}
}
