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
	private final ContaRowMapper contaRowMapper;
	private final ClienteDAO clienteDAO;
	private final SaldoMoedaDAO saldoDAO;
	
	public Conta save(Conta conta) {
		String sql = "INSERT INTO conta(cliente_id, tipo_de_conta, taxa_rendimento, taxa_manutencao) "
				+ "VALUES (?, ?, ?, ?) RETURNING id" ;
		Long id = jdbcTemplate.queryForObject(sql, Long.class,
		        conta.getCliente().getId(),
		        conta.getTipoDeConta().toUpperCase(),
		        conta.getCliente().getTaxaRendimento(),
		        conta.getCliente().getTaxaManutencao()
		    );
		    conta.setId(id);
		    return conta;
//		jdbcTemplate.update(sql, conta.getCliente().getId(), conta.getTipoDeConta(),
//		conta.getCliente().getTaxaRendimento(), conta.getCliente().getTaxaManutencao());
//		return conta;
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
		String sql = "SELECT * FROM conta WHERE ID = ?";
		List<Conta> contas = jdbcTemplate.query(sql, contaRowMapper, id);
		Conta c = contas.get(0);
		if (contas.isEmpty()) return Optional.empty(); //uma linha só, então sem {}
		clienteDAO.findById(c.getClienteId()).ifPresent(c::setCliente);
		c.setSaldos(saldoDAO.findByContaId(c.getId()));
		return Optional.of(c);
	}
	
	public List<Conta> findAll() {
		String sql = "SELECT * FROM conta";
		List<Conta> lista = jdbcTemplate.query(sql, contaRowMapper);
		for(Conta c : lista) {
			clienteDAO.findById(c.getId()).ifPresent(c::setCliente);
			c.setSaldos(saldoDAO.findByContaId(c.getId()));
		}
		return lista;
	}
	
	public void deleteAll(List<Conta> contas) {
		String sql = "DELETE FROM conta WHERE id = ?";
		
		for (Conta conta : contas) {
			jdbcTemplate.update(sql, conta.getId());
		}
	}
	
	public List<Conta> findByClienteId(Long clienteId) {
	    String sql = "SELECT * FROM conta WHERE cliente_id = ?";
	    List<Conta> lista = jdbcTemplate.query(sql, contaRowMapper, clienteId);
	    for(Conta c : lista) {
	    	clienteDAO.findById(c.getClienteId()).ifPresent(c::setCliente);
	    	c.setSaldos(saldoDAO.findByContaId(c.getId()));
	    }
	    return lista;
	}
}
