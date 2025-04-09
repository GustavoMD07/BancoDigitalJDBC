package br.com.cdb.bancodigitalJPA.entity;



import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.NoArgsConstructor;


@Entity
@PrimaryKeyJoinColumn(name = "idCartao")
@NoArgsConstructor
public class CartaoCredito extends Cartao {
	
	//qualquer coisa, usa o JsonIgnore
	//o double primitivo não tava permitindo que o objeto fosse nulo
	
	private BigDecimal limiteCredito;
	
	private BigDecimal fatura;
	
	
	public BigDecimal getLimiteCredito() {
		
		return limiteCredito;
	}

	public void setLimiteCredito(BigDecimal limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public BigDecimal getFatura() {
		return fatura;
	}

	public void setFatura(BigDecimal fatura) {
		this.fatura = fatura;
	}
	
	
	@PrePersist
    @PreUpdate
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.limiteCredito.compareTo(BigDecimal.ZERO) == 0 && getConta() != null && getConta().getCliente() != null) {
            this.limiteCredito = getConta().getCliente().getLimiteCredito();
        }
    }

}
