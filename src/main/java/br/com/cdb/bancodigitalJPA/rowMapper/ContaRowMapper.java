package br.com.cdb.bancodigitalJPA.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import br.com.cdb.bancodigitalJPA.DAO.ClienteDAO;
import br.com.cdb.bancodigitalJPA.DAO.SaldoMoedaDAO;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.entity.SaldoMoeda;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import lombok.RequiredArgsConstructor;


@Component //injeção de dependências btw, facilita para o objeto já pronto
@RequiredArgsConstructor
public class ContaRowMapper implements RowMapper<Conta> {

	private final SaldoMoedaDAO saldoMoedaDAO;
	private final ClienteDAO clienteDAO;
	
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
		long clienteId = rs.getLong("cliente_id");

		Cliente cliente = clienteDAO.findById(clienteId)
	            .orElseThrow(() -> new ObjetoNuloException("Cliente não encontrado"));
	        conta.setCliente(cliente);
	        
		List<SaldoMoeda> saldos = saldoMoedaDAO.findByContaId(conta.getId());
		conta.setSaldos(saldos);
		return conta;
	}

}
