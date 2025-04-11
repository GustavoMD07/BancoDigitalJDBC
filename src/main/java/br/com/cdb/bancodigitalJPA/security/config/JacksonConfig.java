package br.com.cdb.bancodigitalJPA.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration				//criei essa classe por que ele tava bugando muito no TimeStamp pra retornar como erro
public class JacksonConfig {
	
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

       //registrando o modelo do Java
        mapper.registerModule(new JavaTimeModule());

        // Aqui tá o segredo: isso faz ele usar o formato ISO em vez de array
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}
