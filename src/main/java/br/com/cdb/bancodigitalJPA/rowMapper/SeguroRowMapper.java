package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import br.com.cdb.bancodigitalJPA.entity.Seguro;

@Component
public class SeguroRowMapper implements RowMapper<Seguro> {
	
	@Override
	public Seguro mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
		
		Seguro seguro = new Seguro();
		seguro.setId(rs.getLong("id"));
		seguro.setNumeroApolice(rs.getString("numero_apolice"));
		seguro.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
		seguro.setTipoDeSeguro(rs.getString("tipo_de_seguro"));
		seguro.setDescricao(rs.getString("descricao"));
		seguro.setValorApolice(rs.getBigDecimal("valor_apolice"));
		seguro.setAtivo(rs.getBoolean("ativo"));
		seguro.setCartaoId(rs.getLong("cartao_id"));
		
		return seguro;
	}

}
