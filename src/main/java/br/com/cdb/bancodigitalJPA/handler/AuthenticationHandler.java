package br.com.cdb.bancodigitalJPA.handler;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationHandler {
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return new ResponseEntity<>("Usuário não logado no sistema", HttpStatus.UNAUTHORIZED);
		
	}
}
