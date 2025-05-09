package br.com.cdb.bancodigitaljdbc.exception;

public class StatusNegadoException extends RuntimeException{

	private static final long serialVersionUID = -857868102720653000L;

	public StatusNegadoException(String mensagem) {
		super(mensagem);
	}
}
