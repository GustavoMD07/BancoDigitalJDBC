package br.com.cdb.bancodigitalJPA.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component //como o Security já lança o erro sem eu ter muito controle 
// eu deixo como Component, então ele coloca no contexto que precisar
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override //é um método do Handler que já tem no Spring Security, só estou "reescrevendo" ele
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException
	accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");
		
		Map<String, Object> responseBody = new LinkedHashMap<>(); //usando o LinkedHashMap só pra não ter a chance 
        responseBody.put("timestamp", LocalDateTime.now());       //de sair com a ordem errada nos erros
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("error", "Acesso negado");
        responseBody.put("message", "Usuário não tem permissão para acessar esse request");
        responseBody.put("path", request.getRequestURI());
        
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
	}
}
