package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.CartaoDebito;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;

public class CartaoRowMapper implements RowMapper<Cartao>{

	@Override
	public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String tipo = rs.getString("tipo_de_cartao");
		
		Cartao cartao;
		switch (tipo.toUpperCase()) {
		case "CREDITO":
			CartaoCredito cartaoCredito = new CartaoCredito();
			
			cartao = cartaoCredito;
			break;
		case "DEBITO":
			CartaoDebito cartaoDebito = new CartaoDebito();
			cartaoDebito.setLimiteDiario(rs.getBigDecimal("));
			cartao = cartaoDebito;
			break;
		default:
			throw new SubClasseDiferenteException("Tipo de cartão inválido: " + tipo);
		}
		return cartao;
	}
	
}
