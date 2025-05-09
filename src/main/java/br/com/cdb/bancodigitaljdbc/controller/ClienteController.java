package br.com.cdb.bancodigitaljdbc.controller;

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
import org.springframework.web.bind.annotation.RestController;
import br.com.cdb.bancodigitaljdbc.DTO.ClienteDTO;
import br.com.cdb.bancodigitaljdbc.DTO.ClienteResponse;
import br.com.cdb.bancodigitaljdbc.entity.Cliente;
import br.com.cdb.bancodigitaljdbc.exception.ListaVaziaException;
import br.com.cdb.bancodigitaljdbc.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired //mesma coisa, usando o AutoWired pra não me preocupar em instânciar a classe
	private ClienteService clienteService;
	
	//esse ResponseEntity eu uso pra dar alguma resposta a quem fez a requisição
	//é mais pra informar o que tá acontecendo
	@PostMapping("/cliente-security/add")
	public ResponseEntity<String> addCliente(@RequestBody @Valid ClienteDTO clienteDto) {
		Cliente clienteAdicionado = clienteService.addCliente(clienteDto);
		
		if(clienteAdicionado != null) {
			return new ResponseEntity<>("Cliente " + clienteAdicionado.getNome() + 
			" do tipo: " + clienteDto.getTipoDeCliente() + ", adicionado com sucesso",
				HttpStatus.CREATED);
			//esse HttpStatus são aqueles números tipo "404 - not found", "200 - ok", lembra do site dos gatos
		}
		
		else {
			return new ResponseEntity<>("Cliente não foi adicionado ao sistema, "
			+ "operações inválidas", HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@DeleteMapping("/admin-security/remove/{id}")
	public ResponseEntity<String> removeCliente(@PathVariable Long id) {
		
		Cliente clienteRemovido = clienteService.removerCliente(id);
		
		if(clienteRemovido != null) {
			return new ResponseEntity<>("Cliente " + clienteRemovido.getNome() +
			"| ID: " + clienteRemovido.getId() + " removido com sucesso", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Não foi possível encontrar o cliente ", HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/admin-security/atualizar/{id}")
	public ResponseEntity<String> atualizarCliente(@PathVariable Long id,  @RequestBody @Valid ClienteDTO clienteDto) {
		
		Cliente clienteAtualizado = clienteService.atualizarCliente(id, clienteDto);
		if(clienteAtualizado != null) {
			return new ResponseEntity<>("Cliente " + clienteDto.getNome() + " atualizado com sucesso!", HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>("Não foi possível atualizar o cliente ", HttpStatus.NOT_MODIFIED);
		}
	}
	
	@GetMapping("/admin-security/listAll")
	public ResponseEntity<List<ClienteResponse>> listarClientes() {
		List<Cliente> clientes = clienteService.getAllClientes();
		
		List<ClienteResponse> cliente = clientes.stream().map(ClienteResponse::fromEntity).toList();
		if(clientes.isEmpty()) {
			throw new ListaVaziaException("Não foram identificados clientes na lista");
		}
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	@GetMapping("/cliente-security/list/{id}")			//com o PathVariable, eu mostro que o LongId vai ser o que o usuário escreve
	public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Long id) {
		
		Cliente clienteProcurado = clienteService.buscarClientePorId(id);
			return new ResponseEntity<>( ClienteResponse.fromEntity(clienteProcurado), HttpStatus.FOUND);
			
			//o próprio JPA já retorna o cliente com o ID, então eu não preciso usar um List, se eu usasse, eu estaria
			//fazendo algo desnecessário
	}
	
	
}
