package br.com.cdb.bancodigitalJPA.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitalJPA.security.dto.LoginRequest;
import br.com.cdb.bancodigitalJPA.security.jwt.JwtService;
import br.com.cdb.bancodigitalJPA.security.model.Usuario;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController { // aqui eu vou criar os endpoints de autenticação do sistema
	//o usuário passa o JSON do LoginRequest, a gente cria um token, o AuthenticationManager
	//tenta verificar os dados, se der certo, ai a gente gera o JWT(token)
	
	
	@Autowired
	private AuthenticationManager authService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/login") //aqui eu poderia fazer um <?> pra deixar como wildcard também, mas optei por mensagem mesmo
	public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
		try {
			Authentication autenticacao = autenticacao.authenticate( new UsernamePasswordAuthenticationToken(
					request.email(), request.senha()));
			// usernamePassword carrega o email e a senha que o usuário digitou, ai se for válido, ele retorna a instância
			
			
		
			Usuario usuario = (Usuario) autenticacao.getPrincipal();	//cast
			String token = jwtService.generateToken(usuario); //gera o token
			return new ResponseEntity<>("Token: " + token, HttpStatus.OK);
		} catch (AuthenticationException e) {
            return new ResponseEntity<>("Erro ao autenticar: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
	}
}
