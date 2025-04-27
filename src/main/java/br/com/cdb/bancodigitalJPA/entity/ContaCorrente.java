package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {

	private BigDecimal taxaManutencao = BigDecimal.ZERO;

	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaCorrente() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public BigDecimal getTaxaManutencao() {
        return taxaManutencao;
    }
    
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.taxaManutencao.compareTo(BigDecimal.ZERO) == 0 && getCliente() != null) {
            this.taxaManutencao = getCliente().getTaxaManutencao();
        }
    }

}
