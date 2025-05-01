package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import br.com.cdb.bancodigitalJPA.DAO.CartaoDAO;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.Seguro;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeguroRowMapper implements RowMapper<Seguro> {

	private final CartaoDAO cartaoDAO;
	
	@Override
	public Seguro mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Seguro seguro = new Seguro();
		seguro.setId(rs.getLong("id"));
		seguro.setNumeroApolice(rs.getString("numero_apolice"));
		seguro.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
		seguro.setTipoDeSeguro(rs.getString("tipo_de_seguro"));
		seguro.setDescricao(rs.getString("descricao"));
		seguro.setValorApolice(rs.getBigDecimal("valor_apolice"));
		seguro.setAtivo(rs.getBoolean("ativo"));
		
		long cartaoId = rs.getLong("cartao_id");
		Cartao cartao = cartaoDAO.findById(cartaoId).orElseThrow(() -> 
		new ObjetoNuloException("Cartão não encontrado"));;
		seguro.setCartao(cartao);
		
		return seguro;
	}

}
