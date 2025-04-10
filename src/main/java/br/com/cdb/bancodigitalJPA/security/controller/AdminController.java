package br.com.cdb.bancodigitalJPA.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin-security")
public class AdminController {

	@GetMapping("/area")
	public ResponseEntity<String> areaAdmin() {
		return new ResponseEntity<>("Bem vindo a Role Admin", HttpStatus.OK);
	} //apenas um teste pra verificar as roles 
	
	@GetMapping("/test-auth")
	public ResponseEntity<String> test() {
	    return ResponseEntity.ok("Tudo certo, você tá autenticado!");
	}
}
