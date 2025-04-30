package br.com.cdb.bancodigitalJPA.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.cdb.bancodigitalJPA.DAO.CartaoDAO;
import br.com.cdb.bancodigitalJPA.DAO.ClienteDAO;
import br.com.cdb.bancodigitalJPA.DAO.ContaDAO;
import br.com.cdb.bancodigitalJPA.DAO.SaldoMoedaDAO;
import br.com.cdb.bancodigitalJPA.DAO.SeguroDAO;
import br.com.cdb.bancodigitalJPA.DTO.SaldoResponse;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.entity.SaldoMoeda;
import br.com.cdb.bancodigitalJPA.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitalJPA.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.StatusNegadoException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;

@Service
public class ContaService {

	@Autowired
	private ContaDAO contaDAO;

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private CartaoDAO cartaoDAO;

	@Autowired
	private SeguroDAO seguroDAO;

	@Autowired
	private SaldoMoedaDAO saldoMoedaDAO;

	@Autowired
	private RestTemplate restTemplate;
	

	// a conta puxa o cliente, e o cliente puxa o ID

	public Conta addConta(Conta conta) {

		// Recupera o cliente da conta
		Cliente cliente = clienteDAO.findById(conta.getCliente().getId()).orElseThrow(
				() -> new ObjetoNuloException("Cliente com ID " + conta.getCliente().getId() + " não encontrado!"));

		if (cliente.getContas().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}
		
		conta.setCliente(cliente);
		Conta contaSalva = contaDAO.save(conta);
		inicializarSaldos(contaSalva);
		return contaSalva;
	}

	public Conta removerConta(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta.getCartoes() != null && !conta.getCartoes().isEmpty()) {
			for (Cartao cartao : conta.getCartoes()) {
				if (cartao.getSeguros() != null && !cartao.getSeguros().isEmpty()) {
					seguroDAO.deleteAll(cartao.getSeguros());
				}
			}
			cartaoDAO.deleteAll(conta.getCartoes());

		}

		contaDAO.delete(id);
		return conta;
	}

	public Conta buscarContaPorId(Long id) {
		return contaDAO.findById(id).orElseThrow(() -> new ObjetoNuloException("Conta não encontrada"));
	}

	public List<Conta> listarContas() {
		return contaDAO.findAll();
	}

	public List<SaldoResponse> verificarSaldos(Long id) {
		List<SaldoMoeda> saldos = saldoMoedaDAO.findByContaId(id);
		
		if (saldos.isEmpty()) {
			throw new ObjetoNuloException("Nenhum saldo encontrado para esta conta");
		}
		//aqui eu uso Stream, pra transformar a lista de SaldoMoeda
		//em uma lista de SaldoResponse
		 return saldos.stream()
			        .map(s -> new SaldoResponse(s.getMoeda(), s.getSaldo()))
			        .collect(Collectors.toList());
		 
	}

	public void transferir(Long origemid, Long destinoid, BigDecimal valor, String moedaOrigem, String moedaDestino) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		validarMoeda(moedaOrigem);
		validarMoeda(moedaDestino);
		
		SaldoMoeda saldoOrigem = saldoMoedaDAO.findByMoedaAndContaId(moedaOrigem, origemid).orElseThrow(() ->
		    new ObjetoNuloException("Saldo não encontrado para a conta de origem"));
		
		BigDecimal valorConvertido = converterMoeda(valor, moedaOrigem, moedaDestino);
		
		SaldoMoeda saldoDestino = saldoMoedaDAO.findByMoedaAndContaId(moedaDestino, destinoid).orElseGet(() -> {
		        SaldoMoeda novo = new SaldoMoeda();
		        novo.setConta(destino);
		        novo.setMoeda(moedaDestino);
		        novo.setSaldo(BigDecimal.ZERO);
		        return novo;
		    });	
		
		//se o usuário de destino não tem o saldo daquele tipo específico de moeda, eu crio um pra ele, isso ajuda 
				//pra futuramente eu poder adicionar mais moedas
				
			if (origem.getId() == destino.getId()) {
            throw new StatusNegadoException("Não é possível transferir para a mesma conta");
        }
		if (valor.compareTo(saldoOrigem.getSaldo()) > 0) { // ele retorna 1 se o valor for maior que o saldo
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem, revise a sua moeda ");
		}
		

	saldoOrigem.setSaldo(saldoOrigem.getSaldo().subtract(valor)); // subtrai o valor do saldo de origem usando BigDecimal;
	saldoDestino.setSaldo(saldoDestino.getSaldo().add(valorConvertido));

	saldoMoedaDAO.save(saldoOrigem);
	saldoMoedaDAO.save(saldoDestino);

	}

	// por que usar o @Transactional? 1- ele indica que o método vai ser tratado
	// como transação
	// se no meio do processo algo dá errado, por exemplo em saque, ele não modifica
	// nada do saldo e retorna ao que era antes

	public void pix(Long id, BigDecimal valor, String moedaUsada) {
		
		if(valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new StatusNegadoException("Não é possível fazer o pix de um valor negativo ou zero");
		}
		
		validarMoeda(moedaUsada);

	    SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaUsada, id).orElseThrow(() ->
	    new ObjetoNuloException("Saldo não encontrado para a conta de origem"));;

	    if (valor.compareTo(saldo.getSaldo()) > 0) {
	        throw new SaldoInsuficienteException("Saldo insuficiente para fazer o pix");
	    }
	   

	    saldo.setSaldo(saldo.getSaldo().subtract(valor));
	    saldoMoedaDAO.save(saldo);
	}

	public void deposito(Long id, BigDecimal valor, String moedaOrigem, String moedaDestino) {
		validarMoeda(moedaDestino);
		validarMoeda(moedaOrigem);
		Conta conta = buscarContaPorId(id);
		
		if (valor.compareTo(BigDecimal.ZERO) < 0) {
			throw new StatusNegadoException("Valor do depósito deve ser positivo");
		}
		
		BigDecimal valorConvertido = valor;
		
		if(!moedaOrigem.equals(moedaDestino)) {
			valorConvertido = converterMoeda(valor, moedaOrigem, moedaDestino);
		}
		
		 SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaDestino, id).orElseGet(() -> {
		 SaldoMoeda novo = new SaldoMoeda();
		 novo.setConta(conta);
		 novo.setMoeda(moedaDestino);
		 novo.setSaldo(BigDecimal.ZERO);
		 return novo;
		});
	
		saldo.setSaldo(saldo.getSaldo().add(valorConvertido));
		saldoMoedaDAO.save(saldo);
	}

	public void saque(Long id, BigDecimal valor, String moedaUsada, String moedaSacada) {
		validarMoeda(moedaUsada);
		validarMoeda(moedaSacada);
		
		BigDecimal valorConvertido = converterMoeda(valor, moedaUsada, moedaSacada);

	    SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaSacada, id).orElseThrow(() ->
	    new ObjetoNuloException("Saldo não encontrado "));;

	    if (valorConvertido.compareTo(saldo.getSaldo()) > 0) {
	        throw new SaldoInsuficienteException("Saldo insuficiente para saque");
	    }

	    saldo.setSaldo(saldo.getSaldo().subtract(valorConvertido));
	    saldoMoedaDAO.save(saldo);
	}

	public void aplicarTaxaManutencao(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaPoupanca) {
			throw new SubClasseDiferenteException("A taxa de manutenção só pode ser aplicada para contas correntes");
		}
		
		ContaCorrente contaC = (ContaCorrente) conta; // aqui eu faço o cast pra converter tipos de objeto
		// se eu não faço o Cast, eu teria que instanciar a classe de novo, o que não é
		// uma boa prática
		BigDecimal taxa = contaC.getTaxaManutencao();
		
		SaldoMoeda saldoBRL = saldoMoedaDAO.findByMoedaAndContaId("BRL", id).orElseThrow(() ->
	    new ObjetoNuloException("Saldo não encontrado "));

		if (taxa.compareTo(saldoBRL.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para aplicar taxa");
		}

		saldoBRL.setSaldo(saldoBRL.getSaldo().subtract(taxa));
		saldoMoedaDAO.save(saldoBRL);
	}

	public void aplicarRendimento(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaCorrente) {
			throw new SubClasseDiferenteException("Rendimento só pode ser aplicado a contas poupanças");
		}

		ContaPoupanca contaP = (ContaPoupanca) conta;
		BigDecimal taxa = contaP.getTaxaRendimento();
		
		SaldoMoeda saldoBRL = saldoMoedaDAO.findByMoedaAndContaId("BRL", id).orElseThrow(() ->
	    new ObjetoNuloException("Saldo não encontrado"));;

		if (saldoBRL.getSaldo().compareTo(BigDecimal.ZERO) == 0) { // não posso usar + ou * com BigDecimal
			throw new SaldoInsuficienteException("Não é possível aplicar rendimento a um saldo nulo");
		}

		BigDecimal rendimento = saldoBRL.getSaldo().multiply(BigDecimal.ONE.add(taxa));
		saldoBRL.setSaldo(rendimento);
		saldoMoedaDAO.save(saldoBRL);
	}

	
	private void validarMoeda(String moeda) {
		
		if (moeda == null) {
			throw new ObjetoNuloException("Moeda não pode ser nula");
		}
		
		if (!moeda.equals("BRL") && !moeda.equals("USD") && !moeda.equals("EUR")) {
	        throw new StatusNegadoException("Moeda inválida. Nesse momento trabalhamos apenas com: BRL, USD e EUR.");
	    }
	}
	
	private BigDecimal converterMoeda(BigDecimal valor, String moedaOrigem, String moedaDestino) {
		
		if(moedaOrigem.equals(moedaDestino)) {
			return valor;
		} //se a moeda for a mesma, tudo igual
		
		String url = "https://economia.awesomeapi.com.br/json/last/" + moedaOrigem + "-" + moedaDestino;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new ApiBloqueadaException("Erro ao converter moeda");
	    } //se não retornar o início do HTTPS.value 

	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(response.getBody());
	        double taxa = Double.parseDouble(root.path(moedaOrigem + moedaDestino).path("high").asText());
	        return valor.multiply(BigDecimal.valueOf(taxa));
	    } catch (Exception e) {
	        throw new ApiBloqueadaException("Erro ao converter moeda.");
	    }
		//mesma coisa, transforma o JSON em um objeto mapper, ai eu navego por ele com o JsonNode, pego tudo que vier da requisição
	    //depois eu pego a parte do high e retorno ela como Double
	}
	
	private void inicializarSaldos(Conta conta) {
	    List<String> moedas = List.of("BRL", "USD", "EUR");

	    for (String moeda : moedas) {
	        SaldoMoeda saldo = new SaldoMoeda();
	        saldo.setConta(conta);
	        saldo.setMoeda(moeda);
	        saldo.setSaldo(BigDecimal.ZERO);
	        saldoMoedaDAO.save(saldo);
	    }//criando a lista de saldos, por enquanto só esses três mesmo
	}
}
