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
		String sql = "INSERT INTO cartao (senha, status, num_cartao, tipo_de_cartao, conta_id"
			+ "VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, cartao.getSenha(), cartao.isStatus(),
			cartao.getNumCartao(), cartao.getTipoDeCartao(), cartao.getConta().getId());
	}
	
	public void delete(Long id) {
		String sql = "DELETE FROM cartao WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteAll(List<Cartao> cartoes) {
		
		String sql = "DELETE FROM cartao WHERE id = ?";
		
		for(Cartao cartao : cartoes) {
			jdbcTemplate.update(sql, cartao.getId());
		}
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
