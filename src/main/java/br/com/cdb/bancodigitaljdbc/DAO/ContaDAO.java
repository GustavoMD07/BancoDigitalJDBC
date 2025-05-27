package br.com.cdb.bancodigitaljdbc.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.entity.Conta;
import br.com.cdb.bancodigitaljdbc.rowMapper.ContaRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ContaDAO {
	
	private final JdbcTemplate jdbcTemplate;
	private final ContaRowMapper contaRowMapper;
	private final ClienteDAO clienteDAO;
	
	public void save(Conta conta) {
		String sql = "SELECT inserir_conta_v3 (?, ?, ?, ?) ";
		
		Long id = jdbcTemplate.queryForObject(sql, Long.class,
		        conta.getCliente().getId(),
		        conta.getTipoDeConta().toUpperCase(),
		        conta.getCliente().getTaxaRendimento(),
		        conta.getCliente().getTaxaManutencao()
		    );
		    conta.setId(id); //o id é gerado pelo banco, então preciso settar em java

	}
	
	public void delete(Long id) {
		String sql = "SELECT deletar_conta_v1(?)";
		jdbcTemplate.update(sql, id);
	}
	
	public void update(Conta conta) {
		String sql = "SELECT atualizar_conta_v1(?, ?, ?)";
		jdbcTemplate.update(sql,conta.getId(), conta.getCliente().getId(), conta.getTipoDeConta());
		
	}
	
	public Optional<Conta> findById(Long id) {
        String sql = "SELECT * FROM encontrar_conta_v1(?)";
        List<Conta> lista = jdbcTemplate.query(sql, contaRowMapper, id);
        if (lista.isEmpty()) return Optional.empty();

        Conta conta = lista.get(0);
        clienteDAO.findById(conta.getClienteId())
        .ifPresent(conta::setCliente);
        return Optional.of(conta);
    }

	public List<Conta> findAll() {
        String sql = "SELECT * FROM encontrar_todos_conta_v1()";
        return jdbcTemplate.query(sql, contaRowMapper);
    }
	
	public List<Conta> findByClienteId(Long clienteId) {
        String sql = "SELECT * FROM encontrar_conta_por_cliente_v1(?)";
        return jdbcTemplate.query(sql, contaRowMapper, clienteId);
    }
	
	public void deleteAll() {
		String sql = "SELECT deletar_todos_contas_v1()";
		jdbcTemplate.execute(sql);
	}
}
