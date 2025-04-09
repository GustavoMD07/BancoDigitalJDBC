package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "idConta")
public class ContaCorrente extends Conta {

	@Column(precision = 4, scale = 3)
	private BigDecimal taxaManutencao = BigDecimal.ZERO;

	
	// Se você precisa de um construtor, garanta que o Hibernate consiga usar o no-arg constructor
    public ContaCorrente() {
        // O Hibernate precisa de um construtor sem argumentos
    }

    public BigDecimal getTaxaManutencao() {
        return taxaManutencao;
    }
    
    @PrePersist
    @PreUpdate
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.taxaManutencao.compareTo(BigDecimal.ZERO) == 0 && getCliente() != null) {
            this.taxaManutencao = getCliente().getTaxaManutencao();
        }
    }

}
