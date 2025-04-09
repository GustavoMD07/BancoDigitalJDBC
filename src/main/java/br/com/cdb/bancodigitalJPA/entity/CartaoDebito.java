package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.NoArgsConstructor;

@Entity
@PrimaryKeyJoinColumn(name = "idCartao")
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
