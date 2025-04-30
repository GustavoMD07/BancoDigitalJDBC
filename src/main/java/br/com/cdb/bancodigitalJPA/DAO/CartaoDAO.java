package br.com.cdb.bancodigitalJPA.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.rowMapper.CartaoRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CartaoDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	public Cartao save(Cartao cartao) {
		String sql = "INSERT INTO cartao (senha, status, num_cartao, tipo_de_cartao, conta_id"
			+ "VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, cartao.getSenha(), cartao.isStatus(),
		cartao.getNumCartao(), cartao.getTipoDeCartao(), cartao.getConta().getId());
		return cartao;
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
	
	public Optional<Cartao> findById(Long id) {
		String sql = "SELECT * FROM cartao WHERE id = ?";
		List<Cartao> cartao = jdbcTemplate.query(sql, new CartaoRowMapper(), id);
		return cartao.isEmpty() ? Optional.empty() : Optional.of(cartao.get(0));
	}
	//queryForObject só me retorna um resultado, o query retorna uma lista
	//aqui ele retorna só o primeiro do indíce caso ele ache, por isso o uso do Optional
	
	public List<Cartao> findAll() {
		String sql = "SELECT * FROM cartao";
		return jdbcTemplate.query(sql, new CartaoRowMapper());
	}
}
