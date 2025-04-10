package br.com.cdb.bancodigitalJPA.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest( @NotNull @Email String email, @NotNull String senha) { 
	// aqui eu criei um DTO pra receber os dados do login
	// o SpringSecurity espera que eu passe um objeto com o email e a senha
	// o record é uma classe que já tem os getters/setters e o construtor, então não preciso criar 
	// tudo isso de novo, só coloco o nome do atributo e o tipo dele, é uma versão atualizada do Java21 :)
	// nos parâmetros, eu já consigo passar direto o JSON
}
