package br.com.cdb.bancodigitaljdbc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.cdb.bancodigitaljdbc.DAO.CartaoDAO;
import br.com.cdb.bancodigitaljdbc.DAO.ContaDAO;
import br.com.cdb.bancodigitaljdbc.DAO.SeguroDAO;
import br.com.cdb.bancodigitaljdbc.DTO.SeguroDTO;
import br.com.cdb.bancodigitaljdbc.DTO.SeguroResponse;
import br.com.cdb.bancodigitaljdbc.entity.Cartao;
import br.com.cdb.bancodigitaljdbc.entity.CartaoCredito;
import br.com.cdb.bancodigitaljdbc.entity.ClientePremium;
import br.com.cdb.bancodigitaljdbc.entity.Conta;
import br.com.cdb.bancodigitaljdbc.entity.Seguro;
import br.com.cdb.bancodigitaljdbc.exception.ObjetoNuloException;
import br.com.cdb.bancodigitaljdbc.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitaljdbc.utils.SeguroUtils;

@Service
public class SeguroService {
	
	@Autowired
	private SeguroDAO seguroDAO;

	@Autowired
	private CartaoDAO cartaoDAO;
	
	@Autowired
	private ContaDAO contaDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(SeguroService.class);
	public SeguroResponse contratarSeguro(SeguroDTO seguroDto) {
		
		Cartao cartao = cartaoDAO.findById(seguroDto.getCartaoId()).orElseThrow(() ->
	    new ObjetoNuloException("Cartão não encontrado"));
		
		if (!(cartao instanceof CartaoCredito)) {
		    throw new SubClasseDiferenteException("Seguros só podem ser aplicados a cartões de crédito!");
		}
		
		CartaoCredito cartaoCredito = (CartaoCredito) cartao;

		Seguro seguro = new Seguro();
		seguro.setCartao(cartaoCredito);
		seguro.setTipoDeSeguro(seguroDto.getTipoDeSeguro());
		seguro.setDataContratacao(LocalDate.now());
		seguro.setNumeroApolice(SeguroUtils.gerarNumApolice());
		
		Conta conta = contaDAO.findById(cartao.getConta().getId())
			    .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
			cartao.setConta(conta);
		
		// regras de valor e descrição
		if (seguroDto.getTipoDeSeguro().equalsIgnoreCase("viagem")) {

			if (cartao.getConta().getCliente() instanceof ClientePremium) {
				seguro.setValorApolice(BigDecimal.ZERO);
			} 
			else {
				seguro.setValorApolice(BigDecimal.valueOf(50));
			}
			seguro.setDescricao("Cobertura viagem assistência em caso de urgência médica, atrasos de voo, extravio de bagagem e etc");
		} 
		
		else if (seguroDto.getTipoDeSeguro().equalsIgnoreCase("fraude")) {
			seguro.setValorApolice(BigDecimal.ZERO);
			seguro.setDescricao("Cobertura fraude até R$5.000,00 para todos os tipos de cliente");
		}
		
		else {
			throw new SubClasseDiferenteException("Por favor, selecione um tipo válido de seguro: Viagem || Fraude");
		}
		
		seguro.setAtivo(true);
		seguroDAO.save(seguro);
		logger.info("Seguro contratado com sucesso - {}", seguro.getNumeroApolice());
		return SeguroResponse.fromEntity(seguro);
		 //aqui eu salvo o seguro Entity mas retorno o Response, pro usuário conseguir visualizar as informações
	}
	
	public Seguro buscarSeguroPorId(Long id) {
		Seguro s = seguroDAO.findById(id)
	            .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado"));
	        Cartao c = cartaoDAO.findById(s.getCartaoId())
	            .orElseThrow(() -> new ObjetoNuloException("Cartão não encontrado"));
	        s.setCartao(c);
	        return s; // com esse orElseThrow, eu não preciso criar um Optional
	}

	public List<Seguro> listarSeguros() {
        List<Seguro> lista = seguroDAO.findAll();
        for (Seguro s : lista) {
            cartaoDAO.findById(s.getCartaoId()).ifPresent(s::setCartao);
        }
        return lista;
	}
	
	public void cancelarSeguro(Long id) {
	    Seguro seguro = seguroDAO.findById(id)
	        .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado."));
	    seguro.setAtivo(false);
	    seguroDAO.delete(id);
	    logger.warn("seguro cancelado e excluido do database");
	}

}
