package br.com.cdb.bancodigitaljdbc.exception;

public class SubClasseDiferenteException extends RuntimeException {
	private static final long serialVersionUID = -2496941391554239481L;

	public SubClasseDiferenteException(String mensagem) {
		super(mensagem);
	}
}
