package br.com.cdb.bancodigitaljdbc.utils;

import java.security.SecureRandom;

public class CartaoUtils {

	private static final int QntdsNum = 15;
	private static SecureRandom random = new SecureRandom();

	public static final String erroCartao = "Cartão não encontrado";

	public static String gerarNumeroCartao() {

		String num = "";

		for (int i = 0; i < QntdsNum; i++) {
			num += random.nextInt(9);
		}

		int digitoVerificador = calcularDigitoVerificador(num);
		return num + digitoVerificador;
	}

	public static int calcularDigitoVerificador(String num) {

		int soma = 0;
		boolean dobrar = true; // é tipo pegar os ingredientes e somar eles pra ver se tem o suficiente
		// para fazer um bolo...???

		for (int i = num.length() - 1; i >= 0; i--) {
			int numero = Character.getNumericValue(num.charAt(i));

			if (dobrar) {
				numero *= 2;
				if (numero > 9) {
					numero -= 9;
				}
			}
			soma += numero;
			dobrar = !dobrar; // ele inverte o valor do dobrar, ai ele automaticamente vai dobrar um sim,
								// outro não
			// ele começa true, então ele já começa dobrando o último número
		}
		return (10 - (soma % 10));
		// pega o resto da divisão da soma no (soma % 10) e o primeiro 10 subtrai pra
		// ficar o número certo
	}
}
