package br.com.cdb.bancodigitalJPA.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public abstract class Cartao {
	private Long id;
	
	private String senha;
	private boolean status;
	
	private String numCartao;
	
	@JsonBackReference
	protected Conta conta;
	
	@JsonManagedReference
	private List<Seguro> seguros;

	
	public Long getId() {
		return id;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public Boolean getStatus() {
		return status;
	}

	public String getNumCartao() {
		return numCartao;
	}
	
	public String getTipoDeCartao() {
		return this.getClass().getSimpleName().replace("Cartao", "");
	}

	public Conta getConta() {
		return conta;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNumCartao(String numCartao) {
		this.numCartao = numCartao;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public List<Seguro> getSeguros() {
		return seguros;
	}

	public void setSeguros(List<Seguro> seguros) {
		this.seguros = seguros;
	}
	
}
