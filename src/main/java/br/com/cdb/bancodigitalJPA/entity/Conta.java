package br.com.cdb.bancodigitalJPA.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Conta {							
	
	private Long id;
						
	@JsonManagedReference
	private List<SaldoMoeda> saldos = new ArrayList<>();
		
	@JsonBackReference
	private Cliente cliente;
	//esse atributo eu mapeio ele no cliente, depois passo o nome dele no Cliente
	
	@JsonManagedReference 		
	private List<Cartao> cartoes;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Cartao> getCartoes() {
		return cartoes;
	}

	public void setCartao(List<Cartao> cartoes) {
		this.cartoes = cartoes;
	}
	
	public List<SaldoMoeda> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<SaldoMoeda> saldos) {
		this.saldos = saldos;
	}

	
	@JsonProperty		//JsonProperty garante que ele vá aparecer no PostMan
	public String getTipoDeConta() {
		return this.getClass().getSimpleName().replace("Conta", "");
	}
}
