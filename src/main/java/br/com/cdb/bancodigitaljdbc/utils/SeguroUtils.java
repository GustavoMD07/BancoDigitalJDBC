package br.com.cdb.bancodigitaljdbc.utils;

import java.security.SecureRandom;

public class SeguroUtils {

	private static SecureRandom random = new SecureRandom();
	private static final int qntd = 10;

	public static String gerarNumApolice() {

		String num = "";

		for (int i = 0; i < qntd; i++) {
			num += random.nextInt(9);
		}

		return "AP-" + num;
	}
}
