package br.com.cdb.bancodigitaljdbc.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.bancodigitaljdbc.DAO.CartaoDAO;
import br.com.cdb.bancodigitaljdbc.DAO.ContaDAO;
import br.com.cdb.bancodigitaljdbc.DAO.SaldoMoedaDAO;
import br.com.cdb.bancodigitaljdbc.DAO.SeguroDAO;
import br.com.cdb.bancodigitaljdbc.entity.Cartao;
import br.com.cdb.bancodigitaljdbc.entity.CartaoCredito;
import br.com.cdb.bancodigitaljdbc.entity.CartaoDebito;
import br.com.cdb.bancodigitaljdbc.entity.Conta;
import br.com.cdb.bancodigitaljdbc.entity.SaldoMoeda;
import br.com.cdb.bancodigitaljdbc.entity.Seguro;
import br.com.cdb.bancodigitaljdbc.exception.ObjetoNuloException;
import br.com.cdb.bancodigitaljdbc.exception.QuantidadeExcedidaException;
import br.com.cdb.bancodigitaljdbc.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigitaljdbc.exception.StatusNegadoException;
import br.com.cdb.bancodigitaljdbc.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitaljdbc.utils.CartaoUtils;
import br.com.cdb.bancodigitaljdbc.utils.ContaUtils;

@Service
public class CartaoService {

	@Autowired
	private CartaoDAO cartaoDAO;

	@Autowired
	private ContaDAO contaDAO;
	
	@Autowired
	private SaldoMoedaDAO saldoMoedaDAO;
	
	@Autowired
	private SeguroDAO seguroDAO;

	public Cartao addCartao(Cartao cartao) {
		Conta contaEncontrada = contaDAO.findById(cartao.getContaId()).orElseThrow(() -> 
		new ObjetoNuloException(ContaUtils.erroConta));

		if (contaEncontrada.getCartoes().size() >= 2) {
			throw new QuantidadeExcedidaException("O cliente já possui duas contas");
		}

		cartao.setConta(contaEncontrada);
		
		if (cartao instanceof CartaoCredito cc) {
	        // aqui é seguro: conta já existe
	        cc.setLimiteCredito(contaEncontrada.getCliente().getLimiteCredito());
	    }
		
		List<Seguro> seguros = seguroDAO.findByCartaoId(cartao.getId());
		cartao.setSeguros(seguros);
		return cartaoDAO.save(cartao);
	}

	public List<Cartao> listarCartoes() {
		List<Cartao> lista = cartaoDAO.findAll();
        for (Cartao c : lista) {
            Conta conta = contaDAO.findById(c.getContaId())
                .orElseThrow(() -> new ObjetoNuloException(ContaUtils.erroConta));
            c.setConta(conta);

            List<Seguro> seguros = seguroDAO.findByCartaoId(c.getId());
            c.setSeguros(seguros);
        }
        return lista;
	}

	public Cartao desativarCartao(Long id, String senha) {
		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		String senhaEncontrada = cartao.getSenha();
		
		if (senha == null) {
			throw new StatusNegadoException("Digite sua senha");
		}
		
		if (!senha.equals(senhaEncontrada)) {
		    throw new StatusNegadoException("Senha inválida!");
		}
		
		if (!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão já está desativado!");
		}

		if (cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;
			cartaoC.setFatura(BigDecimal.ZERO);

		} else if (cartao instanceof CartaoDebito) {
			CartaoDebito cartaoD = (CartaoDebito) cartao;
			cartaoD.setLimiteDiario(BigDecimal.ZERO);
		}
		
		cartao.setStatus(false);
		cartaoDAO.update(cartao);
		return cartao;
	}

	public Cartao ativarCartao(Long id, String senha) {

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));
		
		String senhaEncontrada = cartao.getSenha();
		
		if (senha == null) {
			throw new StatusNegadoException("Digite sua senha");
		}
		
		if (!senha.equals(senhaEncontrada)) {
		    throw new StatusNegadoException("Senha inválida!");
		}

		if (cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão já está ativado!");
		}
		cartao.setStatus(true);
		cartaoDAO.update(cartao);
		return cartao;
	}

	public void realizarPagamento(Long id, BigDecimal valor, String senha) {
		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		String senhaEncontrada = cartao.getSenha();
		
		if (senha == null) {
			throw new StatusNegadoException("Digite sua senha");
		}
		
		if (!senha.equals(senhaEncontrada)) {
		    throw new StatusNegadoException("Senha inválida!");
		}
		
		if (valor.compareTo(BigDecimal.ZERO) < 0) {
			throw new StatusNegadoException("Não é possível realizar o pagamento de um valor menor que 0");
		}

		if (!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}

		if (cartao instanceof CartaoDebito) {

			CartaoDebito cartaoD = (CartaoDebito) cartao;

			if (cartaoD.getLimiteDiario().compareTo(valor) < 0) {
				throw new SaldoInsuficienteException("O valor excede o limite diário!");
			}

			cartaoD.setLimiteDiario(cartaoD.getLimiteDiario().subtract(valor));
		}

		else if (cartao instanceof CartaoCredito) {
			CartaoCredito cartaoC = (CartaoCredito) cartao;

			if (cartaoC.getFatura().add(valor).compareTo(cartaoC.getLimiteCredito()) > 0 ) {
				throw new SaldoInsuficienteException(
				"Valor ultrapassa o limite de crédito, pague a fatura ou aumente o limite!");
			}

			cartaoC.setFatura(cartaoC.getFatura().add(valor));
		}

		cartaoDAO.update(cartao);
	}

	public void pagarFatura(Long id, BigDecimal valor) {

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		Conta conta = cartao.getConta();
		SaldoMoeda saldo = saldoMoedaDAO.findByMoedaAndContaId("BRL", conta.getId()).orElseThrow(() -> 
		new ObjetoNuloException("Saldo em BRL não encontrado para essa conta"));
		

		if (cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Cartão de débito não possuí fatura, fique tranquilo");
		}

		CartaoCredito cartaoC = (CartaoCredito) cartao;

		if (cartaoC.getFatura().compareTo(BigDecimal.ZERO) == 0) {
			throw new ObjetoNuloException("Não há fatura a ser paga");
		}

		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ObjetoNuloException("Não é possível pagar com o valor abaixo de 0...");
		}

		if (valor.compareTo(cartaoC.getFatura()) > 0) {
			valor = cartaoC.getFatura();
		}

		if (saldo.getSaldo().compareTo(valor) < 0) {
			throw new SaldoInsuficienteException("Saldo da conta insuficiente para pagar a fatura");
		}

		saldo.setSaldo(saldo.getSaldo().subtract(valor));
		cartaoC.setFatura(cartaoC.getFatura().subtract(valor));
		cartaoDAO.update(cartao);
		saldoMoedaDAO.update(saldo);

	}

	public Cartao buscarCartaoPorId(Long id) {
		 Cartao c = cartaoDAO.findById(id)
		            .orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));
		        Conta conta = contaDAO.findById(c.getContaId())
		            .orElseThrow(() -> new ObjetoNuloException(ContaUtils.erroConta));
		        c.setConta(conta);

		        List<Seguro> seguros = seguroDAO.findByCartaoId(c.getId());
		        c.setSeguros(seguros);
		        return c;
	}

	public void alterarLimiteDiario(Long id, BigDecimal novoLimite) {

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		if (!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}

		if (cartao instanceof CartaoCredito) {
			throw new SubClasseDiferenteException("Opção indisponível para cartão de crédito");
		}
		CartaoDebito cartaoD = (CartaoDebito) cartao;
		cartaoD.setLimiteDiario(novoLimite);
		cartaoDAO.update(cartao);
	}

	public void alterarLimiteCredito(Long id, BigDecimal novoLimite) {

		if (novoLimite.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ObjetoNuloException("O novo limite não pode ser menor ou igual a 0");
		}

		if (novoLimite.compareTo(BigDecimal.valueOf(10000)) > 0) {
			throw new QuantidadeExcedidaException("O novo limite não pode ser maior que R$ 10.000,00");
		}
		// aqui eu verifico se o limite é maior que 0 e menor que 10.000,00

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		if (!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}

		if (cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Opção indisponível para cartão de débito");
		}
		CartaoCredito cartaoC = (CartaoCredito) cartao;
		cartaoC.setLimiteCredito(novoLimite);
		cartaoDAO.update(cartao);
	}

	public BigDecimal verificarFatura(Long id) {

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		if (cartao instanceof CartaoDebito) {
			throw new SubClasseDiferenteException("Cartão de Débito não possuí fatura!");
		}

		CartaoCredito cartaoC = (CartaoCredito) cartao;
		return cartaoC.getFatura(); //poderia fazer um .doubleValue pra retornar double se eu quiser
	}

	public void alterarSenha(Long id, String senhaAntiga, String novaSenha) {

		Cartao cartao = cartaoDAO.findById(id)
				.orElseThrow(() -> new ObjetoNuloException(CartaoUtils.erroCartao));

		if (!cartao.isStatus()) {
			throw new StatusNegadoException("Seu cartão está desativado, ative-o para continuar");
		}

		if (!senhaAntiga.equals(cartao.getSenha())) {
			throw new StatusNegadoException("Senha antiga inválida!");
		} // sempre usa o equals pra comparar o conteúdo de strings!

		if (novaSenha.isEmpty()) {
			throw new ObjetoNuloException("A nova senha não pode ser nula");
		}

		if (novaSenha.length() < 8) {
			throw new StatusNegadoException("Sua nova senha precisa ter mais de 8 caracteres!");
		}

		boolean maiuscula = false;
		boolean digito = false;
		boolean caracterEspecial = false;
		String especial = "!_*@#-";
		// eu converto a novaSenha em um array pra conseguir verificar cada coisa que o
		// usuário colocou na senha
		// eu só preciso de uma maiuscula, um número e um especial, então olho todos e
		// caso ALGUM TENHA, eu prossigo
		for (char c : novaSenha.toCharArray()) {
			if (Character.isUpperCase(c)) {
				maiuscula = true;
			}
			if (Character.isDigit(c)) {
				digito = true;
			}
			if (especial.indexOf(c) >= 0) { // Se encontrar o caractere na string especiais
				caracterEspecial = true;
			}
		}

		if (!maiuscula || !digito || !caracterEspecial) {
			throw new StatusNegadoException(
					"Sua nova senha precisa conter pelo menos uma letra maiúscula, um número e um caracter especial (- _ ! * @ #)");
		}

		cartao.setSenha(novaSenha);
		cartaoDAO.update(cartao);
	}
}
