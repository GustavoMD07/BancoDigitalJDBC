package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoMoeda {
	private Long id;
	
	private String moeda; //por enquanto só BRL, EUR e USD
	
	private BigDecimal saldo;
	
	@JsonBackReference //ou JsonIgnore
	private Conta conta;
}
