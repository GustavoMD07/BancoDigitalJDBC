package br.com.cdb.bancodigitaljdbc.utils;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cdb.bancodigitaljdbc.DAO.SaldoMoedaDAO;
import br.com.cdb.bancodigitaljdbc.entity.Conta;
import br.com.cdb.bancodigitaljdbc.entity.SaldoMoeda;
import br.com.cdb.bancodigitaljdbc.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitaljdbc.exception.ObjetoNuloException;
import br.com.cdb.bancodigitaljdbc.exception.StatusNegadoException;

public class ContaUtils {

	public static final String erroConta = "Conta não encontrada";
	
	private static final RestTemplate restTemplate = new RestTemplate();
	
	private static final Logger logger = LoggerFactory.getLogger(ContaUtils.class);
	
	@Autowired
	private static SaldoMoedaDAO saldoMoedaDAO;
	
	public static void validarMoeda(String moeda) {

		if (moeda == null) {
			throw new ObjetoNuloException("Moeda não pode ser nula");
		}

		if (!moeda.equals("BRL") && !moeda.equals("USD") && !moeda.equals("EUR")) {
			throw new StatusNegadoException("Moeda inválida. Nesse momento trabalhamos apenas com: BRL, USD e EUR.");
		}
	}
	
	public static BigDecimal converterMoeda(BigDecimal valor, String moedaOrigem, String moedaDestino) {

		if (moedaOrigem.equals(moedaDestino)) {
			return valor;
		} // se a moeda for a mesma, tudo igual

		String url = "https://economia.awesomeapi.com.br/json/last/" + moedaOrigem + "-" + moedaDestino;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ApiBloqueadaException("Erro ao converter moeda");
		} // se não retornar o início do HTTPS.value

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			double taxa = Double.parseDouble(root.path(moedaOrigem + moedaDestino).path("high").asText());
			return valor.multiply(BigDecimal.valueOf(taxa));
		} catch (Exception e) {
			logger.error("Erro ao fazer requisiçaõ com a api {}", url, e);
			throw new ApiBloqueadaException("Erro ao converter moeda.");
		}
		// mesma coisa, transforma o JSON em um objeto mapper, ai eu navego por ele com
		// o JsonNode, pego tudo que vier da requisição
		// depois eu pego a parte do high e retorno ela como Double
	}
	
	public static void inicializarSaldos(Conta conta) {
		List<String> moedas = List.of("BRL", "USD", "EUR");

		for (String moeda : moedas) {
			SaldoMoeda saldo = new SaldoMoeda();
			saldo.setConta(conta);
			saldo.setMoeda(moeda);
			saldo.setSaldo(BigDecimal.ZERO);
			saldoMoedaDAO.save(saldo);
		} // criando a lista de saldos, por enquanto só esses três mesmo
	}
	
	
}
