package br.com.cdb.bancodigitalJPA.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaldoResponse {
	
	private String moeda;
	private BigDecimal valor;
	
}
