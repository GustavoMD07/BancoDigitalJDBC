package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import br.com.cdb.bancodigitalJPA.DAO.ContaDAO;
import br.com.cdb.bancodigitalJPA.DAO.SeguroDAO;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.CartaoDebito;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.Seguro;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartaoRowMapper implements RowMapper<Cartao>{

	private final SeguroDAO seguroDAO;
	private final ContaDAO contaDAO;
	
	@Override
	public Cartao mapRow(ResultSet rs, int rowNum) throws SQLException {
		
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
		
		List<Seguro> seguros = seguroDAO.findByCartaoId(cartao.getId());
		cartao.setSeguros(seguros);
		
		long contaId = rs.getLong("conta_id");
		
		Conta conta = contaDAO.findById(contaId)
	            .orElseThrow(() -> new ObjetoNuloException("Conta não encontrada"));
	        cartao.setConta(conta);
		return cartao;
	}
	
}
