package br.com.cdb.bancodigitaljdbc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigitaljdbc.DAO.CartaoDAO;
import br.com.cdb.bancodigitaljdbc.DAO.ClienteDAO;
import br.com.cdb.bancodigitaljdbc.DAO.ContaDAO;
import br.com.cdb.bancodigitaljdbc.DAO.SaldoMoedaDAO;
import br.com.cdb.bancodigitaljdbc.DAO.SeguroDAO;
import br.com.cdb.bancodigitaljdbc.DTO.ContaDTO;
import br.com.cdb.bancodigitaljdbc.DTO.SaldoResponse;
import br.com.cdb.bancodigitaljdbc.entity.Cartao;
import br.com.cdb.bancodigitaljdbc.entity.Cliente;
import br.com.cdb.bancodigitaljdbc.entity.Conta;
import br.com.cdb.bancodigitaljdbc.entity.ContaCorrente;
import br.com.cdb.bancodigitaljdbc.entity.ContaPoupanca;
import br.com.cdb.bancodigitaljdbc.entity.SaldoMoeda;
import br.com.cdb.bancodigitaljdbc.exception.ObjetoNuloException;
import br.com.cdb.bancodigitaljdbc.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitaljdbc.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitaljdbc.exception.StatusNegadoException;
import br.com.cdb.bancodigitaljdbc.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitaljdbc.utils.ClienteUtils;
import br.com.cdb.bancodigitaljdbc.utils.ContaUtils;

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

	private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

	@Transactional
	public Conta addConta(ContaDTO dto) {

		Cliente cliente = clienteDAO.findById(dto.getClienteId())
				.orElseThrow(() -> new ObjetoNuloException(ClienteUtils.erroCliente));
		cliente.setContas(contaDAO.findByClienteId(cliente.getId()));

		Conta conta;
		if (dto.getTipoDeConta().equalsIgnoreCase("Corrente")) {
			conta = new ContaCorrente();
		} else {
			conta = new ContaPoupanca();
		}
		conta.setCliente(cliente);

		if (cliente.getContas().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}
		//eu preciso salvar no db antes, por que o ID tá sendo gerado pelo banco
		//logo, se eu preciso dele posteriormente, eu preciso salvar ele no banco antes
		//e só depois, poder usar o ID dele, por que assim ele já vai ter sido gerado
		contaDAO.save(conta);
		inicializarSaldos(conta);

		List<SaldoMoeda> saldos = saldoMoedaDAO.findByContaId(conta.getId());
		conta.setSaldos(saldos);

		List<Cartao> cartoes = cartaoDAO.findByContaId(conta.getId());
		conta.setCartao(cartoes);
		
		logger.info("Conta com o id {} criada", conta.getId());
		return conta;
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
		saldoMoedaDAO.deleteByContaId(id); //apagando manualmente os saldos pra não bugar por conta da FK

		contaDAO.delete(id);
		return conta;
	}

	public Conta buscarContaPorId(Long id) {
		Conta c = contaDAO.findById(id).orElseThrow(() -> new ObjetoNuloException(ContaUtils.erroConta));
		Cliente cliente = clienteDAO.findById(c.getClienteId())
				.orElseThrow(() -> new ObjetoNuloException(ClienteUtils.erroCliente));
		c.setCliente(cliente);

		c.setSaldos(saldoMoedaDAO.findByContaId(c.getId()));
		return c;
	}

	public List<Conta> listarContas() {
		List<Conta> todas = contaDAO.findAll();
		for (Conta c : todas) {
			Cliente cli = clienteDAO.findById(c.getClienteId())
					.orElseThrow(() -> new ObjetoNuloException(ClienteUtils.erroCliente));
			c.setCliente(cli);
			c.setSaldos(saldoMoedaDAO.findByContaId(c.getId()));
			
			List<Cartao> cartoes = cartaoDAO.findByContaId(c.getId());
			c.setCartao(cartoes);
		}	//isso daqui que faz aparecer no Postman a relação dos dois, o cartão depende da conta
		return todas;
	}

	public List<SaldoResponse> verificarSaldos(Long id) {
		List<SaldoMoeda> saldos = saldoMoedaDAO.findByContaId(id);

		if (saldos.isEmpty()) {
			logger.error("uma conta não pode ser inicializada sem saldos, verifique o código");
			throw new ObjetoNuloException("Nenhum saldo encontrado para esta conta");
		}
		// aqui eu uso Stream, pra transformar a lista de SaldoMoeda
		// em uma lista de SaldoResponse
		return saldos.stream().map(s -> new SaldoResponse(s.getMoeda(), s.getSaldo())).collect(Collectors.toList());

	}

	public void transferir(Long origemid, Long destinoid, BigDecimal valor, String moedaOrigem, String moedaDestino) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		ContaUtils.validarMoeda(moedaOrigem);
		ContaUtils.validarMoeda(moedaDestino);

		SaldoMoeda saldoOrigem = saldoMoedaDAO.findByMoedaAndContaId(moedaOrigem, origemid)
				.orElseThrow(() -> new ObjetoNuloException("Saldo não encontrado para a conta de origem"));

		BigDecimal valorConvertido = ContaUtils.converterMoeda(valor, moedaOrigem, moedaDestino);

		SaldoMoeda saldoDestino = saldoMoedaDAO.findByMoedaAndContaId(moedaDestino, destinoid).orElseGet(() -> {
			SaldoMoeda novo = new SaldoMoeda();
			novo.setConta(destino);
			novo.setMoeda(moedaDestino);
			novo.setSaldo(BigDecimal.ZERO);
			return novo;
		});

		// se o usuário de destino não tem o saldo daquele tipo específico de moeda, eu
		// crio um pra ele, isso ajuda
		// pra futuramente eu poder adicionar mais moedas

		if (origem.getId() == destino.getId()) {
			throw new StatusNegadoException("Não é possível transferir para a mesma conta");
		}
		if (valor.compareTo(saldoOrigem.getSaldo()) > 0) { // ele retorna 1 se o valor for maior que o saldo
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem, revise a sua moeda ");
		}

		saldoOrigem.setSaldo(saldoOrigem.getSaldo().subtract(valor)); // subtrai o valor do saldo de origem usando
																		
		saldoDestino.setSaldo(saldoDestino.getSaldo().add(valorConvertido));

		saldoMoedaDAO.update(saldoOrigem);
		saldoMoedaDAO.update(saldoDestino);

	}

	// por que usar o @Transactional? 1- ele indica que o método vai ser tratado
	// como transação
	// se no meio do processo algo dá errado, por exemplo em saque, ele não modifica
	// nada do saldo e retorna ao que era antes

	public void pix(Long id, BigDecimal valor, String moedaUsada) {

		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new StatusNegadoException("Não é possível fazer o pix de um valor negativo ou zero");
		}

		ContaUtils.validarMoeda(moedaUsada);

		SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaUsada, id)
				.orElseThrow(() -> new ObjetoNuloException("Saldo não encontrado para a conta de origem"));
		;

		if (valor.compareTo(saldo.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para fazer o pix");
		}

		saldo.setSaldo(saldo.getSaldo().subtract(valor));
		saldoMoedaDAO.update(saldo);
	}

	public void deposito(Long id, BigDecimal valor, String moedaOrigem, String moedaDestino) {
		ContaUtils.validarMoeda(moedaDestino);
		ContaUtils.validarMoeda(moedaOrigem);
		Conta conta = buscarContaPorId(id);

		if (valor.compareTo(BigDecimal.ZERO) < 0) {
			throw new StatusNegadoException("Valor do depósito deve ser positivo");
		}

		BigDecimal valorConvertido = valor;

		if (!moedaOrigem.equals(moedaDestino)) {
			valorConvertido = ContaUtils.converterMoeda(valor, moedaOrigem, moedaDestino);
		}

		SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaDestino, id).orElseGet(() -> {
			SaldoMoeda novo = new SaldoMoeda();
			novo.setConta(conta);
			novo.setMoeda(moedaDestino);
			novo.setSaldo(BigDecimal.ZERO);
			return novo;
		});

		saldo.setSaldo(saldo.getSaldo().add(valorConvertido));
		saldoMoedaDAO.update(saldo);
	}

	public void saque(Long id, BigDecimal valor, String moedaUsada, String moedaSacada) {
		logger.info("Tentando sacar R${} da conta de id {}", valor, id);
		ContaUtils.validarMoeda(moedaUsada);
		ContaUtils.validarMoeda(moedaSacada);

		BigDecimal valorConvertido = ContaUtils.converterMoeda(valor, moedaUsada, moedaSacada);

		SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId(moedaSacada, id)
				.orElseThrow(() -> new ObjetoNuloException("Saldo não encontrado "));
		;

		if (valorConvertido.compareTo(saldo.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para saque");
		}

		saldo.setSaldo(saldo.getSaldo().subtract(valorConvertido));
		saldoMoedaDAO.update(saldo);
	}

	public void aplicarTaxaManutencao(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaPoupanca) {
			logger.warn("Sub-classes diferente, contaPoupança não pode ser retirada taxa de manutenção");
			throw new SubClasseDiferenteException("A taxa de manutenção só pode ser aplicada para contas correntes");
		}

		ContaCorrente contaC = (ContaCorrente) conta; // aqui eu faço o cast pra converter tipos de objeto
		// se eu não faço o Cast, eu teria que instanciar a classe de novo, o que não é
		// uma boa prática
		BigDecimal taxa = contaC.getTaxaManutencao();

		SaldoMoeda saldoBRL = saldoMoedaDAO.findByMoedaAndContaId("BRL", id)
				.orElseThrow(() -> new ObjetoNuloException("Saldo não encontrado "));

		if (taxa.compareTo(saldoBRL.getSaldo()) > 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente para aplicar taxa");
		}

		saldoBRL.setSaldo(saldoBRL.getSaldo().subtract(taxa));
		saldoMoedaDAO.update(saldoBRL);
	}

	public void aplicarRendimento(Long id) {
		Conta conta = buscarContaPorId(id);

		if (conta instanceof ContaCorrente) {
			logger.warn("Sub-classes diferentes taxa de rendimento disponível apenas para contas poupanças");
			throw new SubClasseDiferenteException("Rendimento só pode ser aplicado a contas poupanças");
		}

		ContaPoupanca contaP = (ContaPoupanca) conta;
		BigDecimal taxa = contaP.getTaxaRendimento();

		SaldoMoeda saldoBRL = saldoMoedaDAO.findByMoedaAndContaId("BRL", id)
				.orElseThrow(() -> new ObjetoNuloException("Saldo não encontrado"));
		;

		if (saldoBRL.getSaldo().compareTo(BigDecimal.ZERO) == 0) { // não posso usar + ou * com BigDecimal
			throw new SaldoInsuficienteException("Não é possível aplicar rendimento a um saldo nulo");
		}

		BigDecimal rendimento = saldoBRL.getSaldo().multiply(BigDecimal.ONE.add(taxa));
		saldoBRL.setSaldo(rendimento);
		saldoMoedaDAO.update(saldoBRL);
	}

	private void inicializarSaldos(Conta conta) {
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
