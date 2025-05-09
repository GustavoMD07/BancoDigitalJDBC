package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.CartaoDebito;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;

@Component
public class CartaoRowMapper implements RowMapper<Cartao>{
	
	@Override
	public Cartao mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
		
		String tipo = rs.getString("tipo_de_cartao");
		
		Cartao cartao;
		switch (tipo.toUpperCase()) {
		case "CREDITO":
			CartaoCredito cartaoCredito = new CartaoCredito();
			cartaoCredito.setFatura(rs.getBigDecimal("fatura"));
			cartaoCredito.setLimiteCredito(rs.getBigDecimal("limite_credito"));
			cartao = cartaoCredito;
			break;
		case "DEBITO":
			CartaoDebito cartaoDebito = new CartaoDebito();
			cartaoDebito.setLimiteDiario(rs.getBigDecimal("limite_diario"));
			cartao = cartaoDebito;
			break;
		default:
			throw new SubClasseDiferenteException("Tipo de cartão inválido: " + tipo);
		}
		
		cartao.setId(rs.getLong("id"));
		cartao.setNumCartao(rs.getString("num_cartao"));
		cartao.setSenha(rs.getString("senha"));
		cartao.setStatus(rs.getBoolean("status"));
		cartao.setContaId(rs.getLong("conta_id"));
		
		return cartao;
	}
	
}
