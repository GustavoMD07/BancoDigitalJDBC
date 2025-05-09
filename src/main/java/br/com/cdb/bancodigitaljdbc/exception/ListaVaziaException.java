package br.com.cdb.bancodigitaljdbc.exception;

public class ListaVaziaException extends RuntimeException{
	private static final long serialVersionUID = 3318321887723492851L;

	public ListaVaziaException(String mensagem) {
		super(mensagem);
	}
}
