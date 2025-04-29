package br.com.cdb.bancodigitalJPA.DAO;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ContaDAO {
	private final JdbcTemplate jdbcTemplate;
	
	public void save(Conta conta) {
		String sql = "INSERT INTO conta()";
		jdbcTemplate.update(sql );
	}
	
	public void delete(Long id) {
		
	}
	
	public void update(Conta conta) {
		
	}
	
	public Conta findById(Long id) {
		Conta conta = null;
		return conta;
	}
	
	public List<Conta> findAll() {
		List<Conta> contas = new ArrayList<>();
		return contas;
	}
}
