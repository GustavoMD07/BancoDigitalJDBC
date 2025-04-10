package br.com.cdb.bancodigitalJPA.security.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority { //aqui eu defino quais são as permissões de segurança

	ROLE_CLIENTE,
	ROLE_ADMIN;
	
	@Override
	public String getAuthority() { // aqui eu retorno o que o sistema espera
		return name();
	} //ele retorna o ROLE_CLIENTE ou o ROLE_ADMIN
}
//em resumo, essa classe vai me retornar qual é a permissão dentro do meu sistema
