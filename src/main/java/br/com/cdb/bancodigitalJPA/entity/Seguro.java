package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seguro {

	private Long id;
	
	private String numeroApolice;
	private LocalDate dataContratacao;
	private String tipoDeSeguro;
	private String descricao;
	private BigDecimal valorApolice;
	private boolean ativo;
	
	@JsonBackReference
	private CartaoCredito cartao;
	
}
