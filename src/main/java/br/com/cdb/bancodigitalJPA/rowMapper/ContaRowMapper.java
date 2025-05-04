package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;


@Component //injeção de dependências btw, facilita para o objeto já pronto
public class ContaRowMapper implements RowMapper<Conta> {
	
	@Override
	public Conta mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String tipo = rs.getString("tipo_de_conta");

		Conta conta;
		switch (tipo.toUpperCase()) {
		case "CORRENTE":
			ContaCorrente contaCorrente = new ContaCorrente();
			contaCorrente.setTaxaManutencao(rs.getBigDecimal("taxa_manutencao"));
			conta = contaCorrente;
			break;
		case "POUPANÇA":
			ContaPoupanca contaPoupanca = new ContaPoupanca();
			contaPoupanca.setTaxaRendimento(rs.getBigDecimal("taxa_rendimento"));
			conta = contaPoupanca;
			break;
		default:
			throw new SubClasseDiferenteException("Tipo de conta inválida: " + tipo);
		}
		
		conta.setId(rs.getLong("id"));
		conta.setClienteId(rs.getLong("cliente_id"));
		return conta;
	}

}
