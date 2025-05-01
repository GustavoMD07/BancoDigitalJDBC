package br.com.cdb.bancodigitalJPA.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.cdb.bancodigitalJPA.DTO.ContaDTO;
import br.com.cdb.bancodigitalJPA.DTO.SaldoResponse;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ListaVaziaException;
import br.com.cdb.bancodigitalJPA.service.ContaService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/contas")
public class ContaController {
	 
	@Autowired
	private ContaService contaService;
	
	
	@PostMapping("/cliente-security/add")
	public ResponseEntity<String> addConta(@RequestBody @Valid ContaDTO contaDto) {
	   contaService.addConta(contaDto);
	    return ResponseEntity
	        .status(HttpStatus.CREATED)
	        .body("Conta " + contaDto.getTipoDeConta() + " adicionada com sucesso");
	}

	
	@DeleteMapping("/admin-security/remove/{id}")
	public ResponseEntity<String> removerConta(@PathVariable Long id) {
		Conta contaRemovida = contaService.removerConta(id);
		
		if(contaRemovida != null) {
			return new ResponseEntity<>("Conta removida com sucesso", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Não foi possível remover a conta de ID: " + id, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/cliente-security/list/{id}")
	public ResponseEntity<Conta> buscarConta(@PathVariable Long id) {
		Conta contaProcurada = contaService.buscarContaPorId(id);
		
		if(contaProcurada != null) {
			return new ResponseEntity<>(contaProcurada, HttpStatus.FOUND);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/admin-security/listAll")
	public ResponseEntity<List<Conta>> listarContas() {
		List<Conta> contas = contaService.listarContas();
		if(contas.isEmpty()) {
			throw new ListaVaziaException("Não foram encontradas Contas.");
		}
		return new ResponseEntity<List<Conta>>(contas, HttpStatus.OK);
	}
	
	@GetMapping("cliente-security/saldo/{id}")
	public ResponseEntity<List<SaldoResponse>> verificarSaldo(@PathVariable Long id) {
		return new ResponseEntity<>(contaService.verificarSaldos(id), HttpStatus.OK);
	}
	
	@PostMapping("/cliente-security/transf/{id}")
	public ResponseEntity<String> transferencia(@PathVariable Long id, @RequestParam Long destinoid, @RequestParam BigDecimal valor, 
			@RequestParam String moedaOrigem, @RequestParam String moedaDestino){
		
		contaService.transferir(id, destinoid, valor, moedaOrigem, moedaDestino);
		return new ResponseEntity<>("Transferência de "+ moedaOrigem + " "+ valor + " para conta de Id " + destinoid + " receber em "
		+ moedaDestino + " realizada com sucesso!", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/cliente-security/pix/{id}")
	public ResponseEntity<String> pix(@PathVariable Long id, @RequestParam BigDecimal valor, @RequestParam String moeda) {
		contaService.pix(id, valor, moeda);
		return new ResponseEntity<>("Pix de " + valor +" realizado com sucesso!\n"
				+ " | Moeda paga: " + moeda, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/cliente-security/deposito/{id}")
	public ResponseEntity<String> deposito(@PathVariable Long id, @RequestParam BigDecimal valor, @RequestParam String moeda, @RequestParam String moedaDepositada) {
		contaService.deposito(id, valor, moeda, moedaDepositada);
		return new ResponseEntity<>("Depósito de " + moeda + " " + valor + 
		" realizado com sucesso\nValor depositado em: " + moedaDepositada, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/cliente-security/saque/{id}")
	public ResponseEntity<String> saque(@PathVariable Long id, @RequestParam BigDecimal valor, @RequestParam String moeda, @RequestParam String moedaSacada) {
		contaService.saque(id, valor, moeda, moedaSacada);
		
		return new ResponseEntity<>("Saque de "+ moeda + valor + 
			" realizado com sucesso\nSaque feito de saldo: " + moedaSacada, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/admin-security/manutencao/{id}")
	public ResponseEntity<String> taxaManutencao(@PathVariable Long id) {
		contaService.aplicarTaxaManutencao(id);
		return new ResponseEntity<>("Taxa de manutenção aplicada", HttpStatus.OK);
	}
	
	@PutMapping("/admin-security/rendimento/{id}")
	public ResponseEntity<String> taxaRendimento(@PathVariable Long id) {
		contaService.aplicarRendimento(id);
		return new ResponseEntity<>("Taxa de rendimento aplicada", HttpStatus.OK);
	}

}
