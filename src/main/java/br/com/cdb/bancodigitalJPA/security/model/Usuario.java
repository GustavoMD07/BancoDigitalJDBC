package br.com.cdb.bancodigitalJPA.security.model;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // lombok pra deixar mais "CleanCode"
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails { // o SpringSecurity entende com o UserDetails que essa classe
// é um usuário do sistema, aqui ele verifica qual é o login, a senha e as roles
// basicamente dizer pro sistema que aqui eu tenho tudo que ele precisa pra autenticar o usuário
	//nessa classe, é onde ele vai ver fazer a autenticação do usuário

	private static final long serialVersionUID = 7509866814604441946L;
	private Long id;

	private String nome;
	private String email;
	private String senha;
	private Role role; // por padrão o Entity lê o enum como 0, 1.

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(role);
	} // quais permissões o usuário vai ter? só uma, o role, de admin ou cliente. Ai
		// eu retorno como lista

	@Override
	public String getPassword() { // comparando as senhas e o nome pra ver se tá tudo certo
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() { // confere se tá tudo ok
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
