package br.com.cdb.bancodigitaljdbc.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitaljdbc.security.model.Usuario;
import br.com.cdb.bancodigitaljdbc.security.repository.UsuarioDAO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService { // ele é responsável por buscar o usuario pelo email no
															// banco
	// e devolver pro Spring Security pra fazer a autenticação
	private final UsuarioDAO usuarioDAO;

	//tentei deixar o método em português, mas ele me força a colocar em inglês por conta da herança
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioDAO.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
	} // ele pega o email, busca no banco, e se não achar, lança uma exceção
	
	
	public boolean existePorEmail(String email) {
		return usuarioDAO.findByEmail(email).isPresent();
	}
	
	public Usuario save(Usuario usuario) {
	    return usuarioDAO.save(usuario);
	}

}
