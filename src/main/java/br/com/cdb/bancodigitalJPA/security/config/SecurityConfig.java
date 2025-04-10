package br.com.cdb.bancodigitalJPA.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import br.com.cdb.bancodigitalJPA.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

@Configuration //essa notação diz que a classe configura algo no projeto
@RequiredArgsConstructor //aqui o lombok deveria injetar o JwtAuthFiller, mas não funcionou
public class SecurityConfig { // autenticação, autorização, e gerencia o fluxo de login

	private final JwtAuthFilter jwtAuthFilter;

	//a notação Bean diz mais ou menos pro Spring criar o objeto, guardar e retornar ele
	//eu usei o bean pra não ter que instanciar o objeto manualmente, ai o Spring já faz isso pra mim
	//é algo parecido com o Autowired pelo que eu entendi
	//e todas essas notações que tem o Bean, é essencial pra autenticação e etc, mas eu não tenho 100% de controle delas, o Spring cuida disso ai
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()) //csfr é desnecessário, pq eu to usando o JWT
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 																				
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll() 
						.anyRequest().authenticated() 
				).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	} //é tipo um filtro de segurança, que eu falo as regras dele
	//sessionManagement não permite que o usuário fique logado pra sempre, então ele vai precisar de um JWT toda vez
	//o authorize serve pra definir quem pode acessar o que, o permitAll eu falo que tudo que começa com /auth pode ser acessado
	//mas o anyRequest já contrapõe isso falando que qualquer outra rota, precisa do usuário logado
	
	
	@Bean //ele é tipo o chefe
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // retorna o manager que vai processar login/autenticação
    } //ele vai validar se o usuário existe e se a senha tá certa, já faz isso automático, pq já é configurado pelo próprio String

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //é só um criptografador de senha padrão, mas é seguro tbm
    }
}
