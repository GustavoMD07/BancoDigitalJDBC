package br.com.cdb.bancodigitalJPA.DAO;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CartaoDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	public void save(Cartao cartao) {

	}
	
	public void delete(Long id) {
		
	}
	
	public void update(Long id) {
		
	}
	
	public Cartao findById(Long id) {
		Cartao cartao = null;
		return cartao;
	}
	
	public List<Cartao> findAll() {
		List<Cartao> cartoes = new ArrayList<>();
		return cartoes;
	}
}
