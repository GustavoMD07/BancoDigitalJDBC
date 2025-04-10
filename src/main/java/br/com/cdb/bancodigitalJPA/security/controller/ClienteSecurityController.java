package br.com.cdb.bancodigitalJPA.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente-security")
public class ClienteSecurityController {

	@GetMapping("/area")
	public ResponseEntity<String> areaCliente() {
		return new ResponseEntity<>("Bem vindo a Role Cliente", HttpStatus.OK);
	} //apenas um teste pra verificar as roles
}
