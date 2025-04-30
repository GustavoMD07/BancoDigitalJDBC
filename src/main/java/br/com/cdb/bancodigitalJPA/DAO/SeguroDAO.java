package br.com.cdb.bancodigitalJPA.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.rowMapper.SeguroRowMapper;
import br.com.cdb.bancodigitalJPA.entity.Seguro;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SeguroDAO {

	private final JdbcTemplate jdbcTemplate;
	
	public void save(Seguro seguro) {
		String sql = "INSER INTO seguro (numeroApolice, dataContratacao, tipoDeSeguro, descricao, valorApolice, ativo)";
		jdbcTemplate.update(sql);
	}
	
	public void delete (Long id) {
		String sql = "DELETE FROM seguro WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteAll(List<Seguro> seguros) {
		String sql = "DELETE FROM seguro WHERE id = ?";
		
		for(Seguro seguro : seguros ) {
			jdbcTemplate.update(sql, seguro.getId());
		}
	}
	
	
	public Optional<Seguro> findById(Long id) {
		String sql = "SELECT * FROM seguro WHERE id = ?";
		List<Seguro> seguro = jdbcTemplate.query(sql, new SeguroRowMapper(), id);
		return seguro.isEmpty() ? Optional.empty() : Optional.of(seguro.get(0));
	}
	
	public List<Seguro> findAll() {
		String sql = "SELECT * FROM seguro";
		return jdbcTemplate.query(sql, new SeguroRowMapper());
	}
}
