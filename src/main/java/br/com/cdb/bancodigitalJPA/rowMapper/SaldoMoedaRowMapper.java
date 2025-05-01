package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.cdb.bancodigitalJPA.entity.SaldoMoeda;

public class SaldoMoedaRowMapper implements RowMapper<SaldoMoeda> {

	@Override
	public SaldoMoeda mapRow(ResultSet rs, int rowNum) throws SQLException {
		 SaldoMoeda saldo = new SaldoMoeda();
	        saldo.setId(rs.getLong("id"));
	        saldo.setMoeda(rs.getString("moeda"));
	        saldo.setSaldo(rs.getBigDecimal("saldo"));
	        return saldo;
		
	}

}
