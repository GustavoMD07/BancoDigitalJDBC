package br.com.cdb.bancodigitalJPA.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.cdb.bancodigitalJPA.DTO.SaldoConvertidoResponse;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitalJPA.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.StatusNegadoException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitalJPA.repository.CartaoRepository;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;
import br.com.cdb.bancodigitalJPA.repository.SeguroRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private RestTemplate restTemplate;

	// a conta puxa o cliente, e o cliente puxa o ID

	public Conta addConta(Conta conta) {

		// Recupera o cliente da conta
		Cliente cliente = clienteRepository.findById(conta.getCliente().getId()).orElseThrow(
				() -> new ObjetoNuloException("Cliente com ID " + conta.getCliente().getId() + " não encontrado!"));

		if (cliente.getContas().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}

		conta.setCliente(cliente);
		return contaRepository.save(conta);
	}

	public Conta removerConta(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta.getCartoes() != null && !conta.getCartoes().isEmpty()) {
			for (Cartao cartao : conta.getCartoes()) {
				if (cartao.getSeguros() != null && !cartao.getSeguros().isEmpty()) {
					seguroRepository.deleteAll(cartao.getSeguros());
				}
			}
			cartaoRepository.deleteAll(conta.getCartoes());

		}

		contaRepository.deleteById(id);
		return conta;
	}

	public Conta buscarContaPorId(Long id) {
		return contaRepository.findById(id).orElseThrow(() -> new ObjetoNuloException("Conta não encontrada"));
	}

	public List<Conta> listarContas() {
		return contaRepository.findAll();
	}

	public BigDecimal verificarSaldo(Long id) {
		return buscarContaPorId(id).getSaldo();
	}

	@Transactional
	public void transferir(Long origemid, Long destinoid, BigDecimal valor) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		if (valor.compareTo(origem.getSaldo()) > 0) { // ele retorna 1 se o valor for maior que o saldo, e ai a gente
														// compara
			// se o valor for maior que o saldo, ele vai retornar 1 e comparar com o 0
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem");
		}

		origem.setSaldo(origem.getSaldo().subtract(valor)); // subtrai o valor do saldo de origem usando BigDecimal;
		destino.setSaldo(destino.getSaldo().add(valor));

		contaRepository.save(origem);
		contaRepository.save(destino); // atualizando as informações
	}
	
	
	// por que usar o @Transactional? 1- ele indica que o método vai ser tratado
	// como transação
	// se no meio do processo algo dá errado, por exemplo em saque, ele não modifica
	// nada do saldo e retorna ao que era antes

	@Transactional
	public void pix(Long id, BigDecimal valor) {
		Conta conta = buscarContaPorId(id);
		if (valor.compareTo(conta.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para fazer o pix");
		}
		conta.setSaldo(conta.getSaldo().subtract(valor));
		contaRepository.save(conta);
	}

	@Transactional
	public void deposito(Long id, BigDecimal valor) {
		Conta conta = buscarContaPorId(id);
		conta.setSaldo(conta.getSaldo().add(valor));
		contaRepository.save(conta);
	}

	@Transactional
	public void saque(Long id, BigDecimal valor) {
		Conta conta = buscarContaPorId(id);
		if (valor.compareTo(conta.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para saque");
		}
		conta.setSaldo(conta.getSaldo().subtract(valor));
		contaRepository.save(conta);
	}

	@Transactional
	public void aplicarTaxaManutencao(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaPoupanca) {
			throw new SubClasseDiferenteException("A taxa de manutenção só pode ser aplicada para contas correntes");
		}
		ContaCorrente contaC = (ContaCorrente) conta; // aqui eu faço o cast pra converter tipos de objeto
		// se eu não faço o Cast, eu teria que instanciar a classe de novo, o que não é
		// uma boa prática
		BigDecimal taxa = contaC.getTaxaManutencao();

		if (taxa.compareTo(contaC.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para aplicar taxa");
		}

		contaC.setSaldo(contaC.getSaldo().subtract(taxa));
		contaRepository.save(contaC);
	}

	@Transactional
	public void aplicarRendimento(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaCorrente) {
			throw new SubClasseDiferenteException("Rendimento só pode ser aplicado a contas poupanças");
		}

		ContaPoupanca contaP = (ContaPoupanca) conta;
		BigDecimal taxa = contaP.getTaxaRendimento();

		if (conta.getSaldo().compareTo(BigDecimal.ZERO) == 0) { // não posso usar + ou * com BigDecimal
			throw new SaldoInsuficienteException("Não é possível aplicar rendimento a um saldo nulo");
		}

		BigDecimal rendimento = contaP.getSaldo().multiply(BigDecimal.ONE.add(taxa));
		contaP.setSaldo(rendimento);
//		contaP.setSaldo(contaP.getSaldo() * (1 + taxa));
		contaRepository.save(contaP);
	}

	public SaldoConvertidoResponse saldoConvertido(Long id) {
		Conta conta = buscarContaPorId(id);
		BigDecimal saldoBRL = conta.getSaldo();

		String url = "https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ApiBloqueadaException("Erro ao consultar API de câmbio");
		}
		try {
			ObjectMapper mapper = new ObjectMapper(); // converte o JSON em java normal, só pra ler a resposta da API
			JsonNode root = mapper.readTree(response.getBody()); //transforma o JSON da resposta em uma "arvore de dados"
			//eu uso ele pra caminhar no JSON da URL, o path fala pra ele ir até USDBRL e me retornar como texto o campo high
			double taxaUSD = Double.parseDouble(root.path("USDBRL").path("high").asText());
			double taxaEUR = Double.parseDouble(root.path("EURBRL").path("high").asText());
			//e retornando como texto, eu faço a conversão pra Double
			
			//pego o saldo em reais, divido pela taxa em dolar, quero 2 casas decimais e arredondar pra cima só se for + 0.5 :)
			BigDecimal saldoUSD = saldoBRL.divide(BigDecimal.valueOf(taxaUSD), 2, RoundingMode.HALF_UP);
			BigDecimal saldoEUR = saldoBRL.divide(BigDecimal.valueOf(taxaEUR), 2, RoundingMode.HALF_UP);
			return new SaldoConvertidoResponse(saldoBRL, saldoUSD, saldoEUR);
			
		} catch (Exception e) {
			throw new ApiBloqueadaException("Erro ao processar os dados de câmbio");
		}
	}
	
	@Transactional
	public void transferirInternacional(Long origemid, Long destinoid, BigDecimal valor, String moedaDestino) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		
		
		if (valor.compareTo(origem.getSaldo()) > 0) { // ele retorna 1 se o valor for maior que o saldo, e ai a gente
													  // compara  se o valor for maior que o saldo, ele vai retornar 1 e comparar com o 0
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem");
		}
		
		String url = "https://economia.awesomeapi.com.br/json/last/" + moedaDestino + "-BRL";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		 if (!response.getStatusCode().is2xxSuccessful()) {
		        throw new ApiBloqueadaException("Erro ao consultar taxa de câmbio.");
		    }
		 
		 if(!moedaDestino.equals("USD") && !moedaDestino.equals("EUR")) {
			 throw new StatusNegadoException("Nosso sistema só permite a conversão para euro e dólar por enquanto.");
		 }
		 
		 try {
			 
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			double taxa = Double.parseDouble(root.path(moedaDestino + "BRL").path("high").asText());
			
			BigDecimal valorConvertido = valor.divide(BigDecimal.valueOf(taxa), 2, RoundingMode.HALF_UP);
			
			origem.setSaldo(origem.getSaldo().subtract(valor));
			destino.setSaldo(destino.getSaldo().add(valorConvertido));			
			
			//se eu pagar com o meu dinheiro em reais, o usuário de fora recebe o valor em dolar
			
			contaRepository.save(origem);
			contaRepository.save(destino); // atualizando as informações
		 } catch(Exception e) {
			 throw new ApiBloqueadaException("Erro ao realizar a conversão da moeda para a transferência. Moedas disponíveis: BRL, USD, EUR");
		 }

		
	}

}
