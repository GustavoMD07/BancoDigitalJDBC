package br.com.cdb.bancodigitaljdbc.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeguroDTO {

	@NotNull
	private Long cartaoId;
	
	@NotNull
	private String tipoDeSeguro;
}
