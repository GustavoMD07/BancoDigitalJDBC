package br.com.cdb.bancodigitaljdbc.security.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest( @NotBlank String nome, @NotBlank @Email String email, 
		@NotBlank String senha, @NotBlank String role) {

}
