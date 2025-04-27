package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartaoDebito extends Cartao {

	private BigDecimal limiteDiario = BigDecimal.valueOf(500);
	
	public BigDecimal getLimiteDiario() {
		return limiteDiario;
	}


	public void setLimiteDiario(BigDecimal limiteDiario) {
		this.limiteDiario = limiteDiario;
	}

}
