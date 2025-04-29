package br.com.cdb.bancodigitalJPA.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
		
	}
	
	public void update (Long id) {
		
	}
	
	public Seguro findById(Long id) {
		Seguro seguro = null;
		return seguro;
	}
	
	public List<Seguro> findAll() {
		List<Seguro> seguros = new ArrayList<>();
		return seguros;
	}
}
