package br.com.cdb.bancodigitalJPA.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.DAO.CartaoDAO;
import br.com.cdb.bancodigitalJPA.DAO.SeguroDAO;
import br.com.cdb.bancodigitalJPA.DTO.SeguroDTO;
import br.com.cdb.bancodigitalJPA.DTO.SeguroResponse;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.Seguro;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitalJPA.repository.SeguroRepository;

@Service
public class SeguroService {

	private SecureRandom random = new SecureRandom();
	private static final int qntd = 10;
	
	@Autowired
	private SeguroDAO seguroDAO;

	@Autowired
	private CartaoDAO cartaoDAO;

	public SeguroResponse contratarSeguro(SeguroDTO seguroDto) {
		
		Cartao cartao = cartaoDAO.findById(seguroDto.getCartaoId());
		
		if (!(cartao instanceof CartaoCredito)) {
		    throw new SubClasseDiferenteException("Seguros só podem ser aplicados a cartões de crédito!");
		}
		
		CartaoCredito cartaoCredito = (CartaoCredito) cartao;

		Seguro seguro = new Seguro();
		seguro.setCartao(cartaoCredito);
		seguro.setTipoDeSeguro(seguroDto.getTipoDeSeguro());
		seguro.setDataContratacao(LocalDate.now());
		seguro.setNumeroApolice(gerarNumApolice());
		
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
		return SeguroResponse.fromEntity(seguro);
		 //aqui eu salvo o seguro Entity mas retorno o Response, pro usuário conseguir visualizar as informações
	}
	
	public SeguroResponse buscarSeguroPorId(Long id) {
	    Seguro seguro = seguroDAO.findById(id)
	        .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado"));
	    return SeguroResponse.fromEntity(seguro); // com esse orElseThrow, eu não preciso criar um Optional
	}

	public List<SeguroResponse> listarSeguros() {
	    
		List<Seguro> seguros = seguroDAO.findAll();
		if(seguros.isEmpty()) {
			throw new ObjetoNuloException("Não foram encontrados seguros no sistema");
		}
		
		return seguros.stream().map(SeguroResponse::fromEntity).toList();
	    
	    //o stream "processa" todos os dados
	    //.map converte cada seguro em seguroResponse pelo fromEntity
	    //toList retorna os dados em uma lista
	}

	public void cancelarSeguro(Long id) {
	    Seguro seguro = seguroDAO.findById(id)
	        .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado."));
	    seguro.setAtivo(false);
	    seguroDAO.save(seguro);
	}

	private String gerarNumApolice() {
	    
		String num = "";
		
		for(int i = 0; i < qntd; i++) {
			num += random.nextInt(9);
		}
		
		return "AP-" + num;
	}
}
