package br.com.cdb.bancodigitaljdbc.exception;

public class ApiBloqueadaException extends RuntimeException {
	private static final long serialVersionUID = 4616126302068753005L;
	
	public ApiBloqueadaException(String mensagem) {
		super(mensagem);
	}

}
