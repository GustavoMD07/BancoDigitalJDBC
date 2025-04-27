package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;

public class ContaPoupanca extends Conta {

	private BigDecimal taxaRendimento = BigDecimal.ZERO;
	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaPoupanca() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public BigDecimal getTaxaRendimento() {
        return taxaRendimento;
    }
    
    public void calcularLimiteAntesDeSalvar() {
        if (this.taxaRendimento.compareTo(BigDecimal.ZERO) == 0 && getCliente() != null) {
            this.taxaRendimento = getCliente().getTaxaRendimento();
        }
    }

}
