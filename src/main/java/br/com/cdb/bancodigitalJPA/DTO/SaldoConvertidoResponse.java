package br.com.cdb.bancodigitalJPA.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaldoConvertidoResponse {
	
	private BigDecimal saldoBRL;
	private BigDecimal saldoUSD;
	private BigDecimal saldoEUR;
	
	@Override
	public String toString() {
		return "\n BRL: " + saldoBRL + 
			       " | USD: " + saldoUSD + 
			       " | EUR: " + saldoEUR;
	}
}
