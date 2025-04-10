package br.com.cdb.bancodigitalJPA.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Suporte a datas Java 8
        mapper.registerModule(new JavaTimeModule());

        // Aqui tá o segredo: isso faz ele usar o formato ISO (string bonitinha) em vez de array
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
