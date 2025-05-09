package br.com.cdb.bancodigitaljdbc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	private static final String url = "jdbc:mysql://localhost:5432/bancodigital";
	private static final String user = "postgres";
	private static final String password = "240307";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
