package br.com.cdb.bancodigitalJPA.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AcessDeniedHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAcessDenied(AccessDeniedException ex) {
		return new ResponseEntity<>("Acesso negado. Sem permissão para prosseguir com o Request", HttpStatus.UNAUTHORIZED);
	} // aqui eu retorno o erro 403, que é o erro de acesso negado
}
