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
        String sql = "INSERT INTO saldo_moeda (moeda, saldo, conta_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
            saldo.getMoeda(),
            saldo.getSaldo(),
            saldo.getConta().getId()
        );
    }
    
    public void update(SaldoMoeda saldo) {
        String sql = "UPDATE saldo_moeda SET saldo = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            saldo.getSaldo(),
            saldo.getId()
        );
    }

    public List<SaldoMoeda> findByContaId(Long contaId) {
        String sql = "SELECT * FROM saldo_moeda WHERE conta_id = ?";
        return jdbcTemplate.query(sql, saldoMapper, contaId);
    }

    // apagar todos os saldos de uma conta
    public void deleteByContaId(Long contaId) {
        String sql = "DELETE FROM saldo_moeda WHERE conta_id = ?";
        jdbcTemplate.update(sql, contaId);
    }
    
   public Optional<SaldoMoeda> findByMoedaAndContaId(String moeda, Long contaId) {
	   String sql = "SELECT * FROM saldo_moeda WHERE conta_id = ? AND moeda = ?";
	   List<SaldoMoeda> saldo = jdbcTemplate.query(sql, saldoMapper,contaId, moeda );
	   return saldo.isEmpty() ? Optional.empty() : Optional.of(saldo.get(0));
	   
   }
}
	

