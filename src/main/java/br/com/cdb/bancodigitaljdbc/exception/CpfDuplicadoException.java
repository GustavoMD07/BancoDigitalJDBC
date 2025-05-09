package br.com.cdb.bancodigitaljdbc.exception;


public class CpfDuplicadoException extends RuntimeException {
	private static final long serialVersionUID = -4883056717742175135L;
	
	public CpfDuplicadoException(String mensagem) {
		super(mensagem);
	}

}
