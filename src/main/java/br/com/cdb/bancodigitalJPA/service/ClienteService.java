package br.com.cdb.bancodigitalJPA.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.cdb.bancodigitalJPA.DAO.ClienteDAO;
import br.com.cdb.bancodigitalJPA.DAO.ContaDAO;
import br.com.cdb.bancodigitalJPA.DTO.ClienteDTO;
import br.com.cdb.bancodigitalJPA.DTO.ClienteResponse;
import br.com.cdb.bancodigitalJPA.DTO.EnderecoResponse;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.ClienteComum;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.ClienteSuper;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitalJPA.exception.CpfDuplicadoException;
import br.com.cdb.bancodigitalJPA.exception.IdadeInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;

@Service
public class ClienteService {

	// com o AutoWired eu não preciso me preocupar com a criação desse objeto
	// quando for construido o objeto do Repository, ele vai fazer um new quando precisar
	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
	private ContaDAO contaDAO;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	// RestTemplate é um "navegador", eu uso ele pra poder me comunicar com alguma
	// API que seja externa ao meu sistemaz

	// aqui então você precisa validar os campos primeiro
	// geralmente você usa o próprio Objeto, é uma boa prática:)
	public Cliente addCliente(ClienteDTO clienteDto) {
		// pego a data de nascimento, depois pego a data de hoje e comparo
		LocalDate dataNascimento = clienteDto.getDataNascimento();
		LocalDate hoje = LocalDate.now();

		Integer idade = Period.between(dataNascimento, hoje).getYears();

		if (idade < 18) {
			throw new IdadeInsuficienteException("O cliente deve ter 18 anos ou mais para criar a conta!");
		}

		Cliente cliente;

		if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Comum")) {
			cliente = new ClienteComum();
		}

		else if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Super")) {
			cliente = new ClienteSuper();
		}

		else if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Premium")) {
			cliente = new ClientePremium();
		} else {
			throw new SubClasseDiferenteException("Selecione o tipo de cliente: Comum || Super || Premium");
		}

		cliente.setCpf(clienteDto.getCPF());
		cliente.setNome(clienteDto.getNome());
		cliente.setDataNascimento(clienteDto.getDataNascimento());
		Optional<Cliente> clienteExistente = clienteDAO.findByCPF(cliente.getCpf());
		if (clienteExistente.isPresent()) {
		    throw new CpfDuplicadoException("Já existe um cliente com este CPF cadastrado.");
		}

		EnderecoResponse endereco = buscarEnderecoPorCep(clienteDto.getCep());

		cliente.setCep(endereco.getCep());
		cliente.setCidade(endereco.getCidade());
		cliente.setEstado(endereco.getEstado());
		cliente.setBairro(endereco.getBairro());
		cliente.setRua(endereco.getRua());

		return clienteDAO.save(cliente);
		// esse .save é um método do próprio JPA, por isso ele facilita TANTO a vida do
		// programador
		// antes eu só preciso criar o objeto Cliente ( você só cria o objeto UMA VEZ) e
		// passar os parâmetros
		// necessários pra salvar
	}

	// quando eu quero usar o método de buscar o cliente por ID, eu preciso usar o
	// Optional, por que ele evita
	// que o método de erro, ele
	public Cliente removerCliente(Long id) {
		Cliente cliente = buscarClientePorId(id);
		List<Conta> contas = contaDAO.findByClienteId(cliente.getId());
		contaDAO.deleteAll(contas);
		clienteDAO.delete(id);
		return cliente;
	}

	 public Cliente atualizarCliente(Long id, ClienteDTO dto) {
	   
	        buscarClientePorId(id);

	        
	        Cliente cliente;
	        String tipo = dto.getTipoDeCliente().toUpperCase();
	        switch (tipo) {
	            case "COMUM":
	            	cliente = new ClienteComum();
	                break;
	            case "SUPER":
	            	cliente = new ClienteSuper();
	                break;
	            case "PREMIUM":
	            	cliente = new ClientePremium();
	                break;
	            default:
	                throw new SubClasseDiferenteException("Selecione o tipo de cliente: Comum, Super ou Premium");
	        }
	        cliente.setId(id);

	        // 3) Copia os outros campos
	        cliente.setNome(dto.getNome());
	        cliente.setCpf(dto.getCPF());
	        cliente.setDataNascimento(dto.getDataNascimento());

	        EnderecoResponse end = buscarEnderecoPorCep(dto.getCep());
	        cliente.setCep(end.getCep());
	        cliente.setRua(end.getRua());
	        cliente.setBairro(end.getBairro());
	        cliente.setCidade(end.getCidade());
	        cliente.setEstado(end.getEstado());

	        // 4) Atualiza no banco
	        return clienteDAO.update(cliente);
	 }

	public List<Cliente> getAllClientes() {
		List<Cliente> clientes = clienteDAO.findAll();
		for (Cliente c : clientes) {
			c.setContas(contaDAO.findByClienteId(c.getId()));
		}
		return clientes;
	}

	public Cliente buscarClientePorId(Long id) {
		Cliente c = clienteDAO.findById(id).orElseThrow(() -> 
			new ObjetoNuloException("Cliente não encontrado"));
		List<Conta> contas = contaDAO.findByClienteId(c.getId());
		c.setContas(contas);
		return c;
	}

	private EnderecoResponse buscarEnderecoPorCep(String cep) {
		try {
			String url = "https://brasilapi.com.br/api/cep/v1/" + cep;
			ResponseEntity<EnderecoResponse> response = restTemplate.getForEntity(url, EnderecoResponse.class);
			// pego o corpo do EnderecoResponse, a url é onde eu quero pegar os dados pra
			// uso o restTemplate pra ele me trazer a resposta convertida pro tipo de EnderecoResponse

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody();
				// dos HTTP, se o retorno começa com 2, eu posso continuar, então to verificando
				// se eu consigo
				// também verifico se o corpo é nulo, se for, tem algo errado
			} else {
				throw new ObjetoNuloException("CEP inválido");
			}
		} catch (Exception e) {
			throw new ApiBloqueadaException("Erro ao integrar com a APi" + e.getMessage());
		}
	}

}
