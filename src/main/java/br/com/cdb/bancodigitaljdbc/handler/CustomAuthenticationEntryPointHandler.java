package br.com.cdb.bancodigitaljdbc.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");

		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("timestamp", LocalDateTime.now());
		responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
		responseBody.put("error", "Não autorizado");
		responseBody.put("message", "Antes de usar qualquer request, é necessário estar logado no sistema");
		responseBody.put("path", request.getRequestURI());

		response.getWriter().write(objectMapper.writeValueAsString(responseBody));
	}
}
