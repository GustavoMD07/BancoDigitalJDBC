package br.com.cdb.bancodigitaljdbc.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigitaljdbc.entity.SaldoMoeda;

@Component
public class SaldoMoedaRowMapper implements RowMapper<SaldoMoeda> {

	@Override
	public SaldoMoeda mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
		 SaldoMoeda saldo = new SaldoMoeda();
	        saldo.setId(rs.getLong("id"));
	        saldo.setMoeda(rs.getString("moeda"));
	        saldo.setSaldo(rs.getBigDecimal("saldo"));
	        return saldo;
		
	}

}
