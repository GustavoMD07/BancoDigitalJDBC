package br.com.cdb.bancodigitalJPA.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.security.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

//esse é tipo o porteiro da segurança, ele gera o token, valida e pega o email do usuário do token

@Service
public class JwtService {

	private final String ChaveSecreta = Base64.getEncoder().encodeToString("m3uSegr3doSup3rF0rte!@#1234567890".getBytes());
	
	private Key getChaveAssinatura() {
		return Keys.hmacShaKeyFor(ChaveSecreta.getBytes());
	}
	
	public String gerarToken(Usuario usuario) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", usuario.getRole().name()); // role do usuário no token
		
		return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getChaveAssinatura(), SignatureAlgorithm.HS256)
                .compact();
	}
	
	//aqui eu to usando o T como um coringa, por que posso querer extrair o email, ou a data de expiração
	//posteriormente eu vou fazer modificações
	public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extrairTodosOsClaims(token);
		return claimsResolver.apply(claims);
	} // ele pega o token, e devolve o que eu pedi (o email ou qualquer outra coisa)
	
	public String extrairUsername(String token) {
		return extrairClaim(token, Claims::getSubject);
	}
	
	private Claims extrairTodosOsClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getChaveAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody();
    } // ele gera o token, e coloca o email dentro do token, depois assina com a chave secreta
	
	private Date extrairExpiration(String token) {
		return extrairClaim(token, Claims::getExpiration);
	}
	
	public boolean tokenExpirado(String token) {
		return extrairExpiration(token).before(new Date());
	}
	
	
	public boolean tokenValido(String token, String username) {
		final String usernameEncontrado = extrairUsername(token);
		return (usernameEncontrado.equals(username) && !tokenExpirado(token));
	}
		
}
