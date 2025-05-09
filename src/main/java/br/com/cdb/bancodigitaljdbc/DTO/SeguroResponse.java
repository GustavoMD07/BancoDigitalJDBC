package br.com.cdb.bancodigitaljdbc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.cdb.bancodigitaljdbc.entity.Seguro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguroResponse {

	private Long id;
	private String tipoDeSeguro;
	private LocalDate dataContratacao;
	private String numeroApolice;
	private String descricao;
	private Long cartaoId;
	private boolean ativo;
	private BigDecimal valorApolice;
	
	//fromEntity para transformar o Seguro no SeguroResponse, pro usuário conseguir ter uma visibilidade melhor do código
	
	public static SeguroResponse fromEntity(Seguro seguro) {
	    SeguroResponse response = new SeguroResponse();
	    response.setId(seguro.getId());
	    response.setNumeroApolice(seguro.getNumeroApolice());
	    response.setDataContratacao(seguro.getDataContratacao());
	    response.setTipoDeSeguro(seguro.getTipoDeSeguro());
	    response.setValorApolice(seguro.getValorApolice());
	    response.setDescricao(seguro.getDescricao());
	    response.setAtivo(seguro.isAtivo());
	    response.setCartaoId(seguro.getCartao().getId());
	    return response;
	}
}
