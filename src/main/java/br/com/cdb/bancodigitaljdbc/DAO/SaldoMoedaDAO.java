package br.com.cdb.bancodigitaljdbc.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.entity.SaldoMoeda;
import br.com.cdb.bancodigitaljdbc.rowMapper.SaldoMoedaRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SaldoMoedaDAO {

	private final JdbcTemplate jdbcTemplate;
	private final SaldoMoedaRowMapper saldoMapper;

	// inserir novo saldo
	public void save(SaldoMoeda saldo) {
		String sql = "CALL inserir_saldo_v1 (?, ?, ?)";
		jdbcTemplate.update(sql, saldo.getMoeda(), saldo.getSaldo(), saldo.getConta().getId());
	}

	public void update(SaldoMoeda saldo) {
		String sql = "SELECT atualizar_saldo_v2()";
		jdbcTemplate.update(sql, saldo.getSaldo(), saldo.getId());
	}

	public List<SaldoMoeda> findByContaId(Long contaId) {
		String sql = "SELECT * FROM encontrar_saldo_por_conta_v1(?)";
		return jdbcTemplate.query(sql, saldoMapper, contaId);
	}

	public void deleteByContaId(Long contaId) {
		String sql = "SELECT deletar_saldo_por_conta_v1(?)";
		jdbcTemplate.queryForObject(sql, Boolean.class, contaId);
	}

	public Optional<SaldoMoeda> findByMoedaAndContaId(String moeda, Long contaId) {
		String sql = "SELECT * FROM encontrar_saldo_por_moeda_v1(?, ?)";
		List<SaldoMoeda> saldo = jdbcTemplate.query(sql, saldoMapper, moeda, contaId);
		return saldo.isEmpty() ? Optional.empty() : Optional.of(saldo.get(0));

	}
	
	
}
