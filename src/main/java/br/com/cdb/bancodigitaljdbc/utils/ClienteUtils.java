package br.com.cdb.bancodigitaljdbc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.com.cdb.bancodigitaljdbc.DTO.EnderecoResponse;
import br.com.cdb.bancodigitaljdbc.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitaljdbc.exception.ObjetoNuloException;

public class ClienteUtils {

	private static final RestTemplate restTemplate = new RestTemplate();
	
	public static final String erroCliente = "Cliente não encontrado";
	
	private static final Logger logger = LoggerFactory.getLogger(ClienteUtils.class);

	public static EnderecoResponse buscarEndereco(String cep) {
		//static por que só tem um método mesmo, facilita em vez de instanciar
		try {
			String url = "https://brasilapi.com.br/api/cep/v1/" + cep;
			ResponseEntity<EnderecoResponse> response = restTemplate.getForEntity(url, EnderecoResponse.class);
			// pego o corpo do EnderecoResponse, a url é onde eu quero pegar os dados pra
			// uso o restTemplate pra ele me trazer a resposta convertida pro tipo de
			// EnderecoResponse

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody();
				// dos HTTP, se o retorno começa com 2, eu posso continuar, então to verificando
				// se eu consigo
				// também verifico se o corpo é nulo, se for, tem algo errado
			} else {
				throw new ObjetoNuloException("CEP inválido");
			}
		} catch (Exception e) {
			logger.error("Erro na requisição da API", e);
			throw new ApiBloqueadaException("Erro ao integrar com a APi" + e.getMessage());
		}
	}
}
