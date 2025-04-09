package br.com.cdb.bancodigitalJPA.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // lombok pra deixar mais "CleanCode"
@AllArgsConstructor
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String login;
	private String senha;

	@Enumerated(EnumType.STRING) // salvando os Enums como String pra aparecer certo no banco
	private Role role; // por padrão o Entity lê o enum como 0, 1.

}
