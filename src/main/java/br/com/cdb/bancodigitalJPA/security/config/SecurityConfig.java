package br.com.cdb.bancodigitalJPA.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import br.com.cdb.bancodigitalJPA.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

@Configuration // essa notação diz que a classe configura algo no projeto
@EnableWebSecurity // habilita a segurança na aplicação
@RequiredArgsConstructor // aqui o lombok deveria injetar o JwtAuthFiller, mas não funcionou
public class SecurityConfig { // autenticação, autorização, e gerencia o fluxo de login
	// basicamente as configurações gerais, o que cada role pode ou não fazer, etc

	private final JwtAuthFilter jwtAuthFilter;

	// a notação Bean diz mais ou menos pro Spring criar o objeto, guardar e
	// retornar ele
	// eu usei o bean pra não ter que instanciar o objeto manualmente, ai o Spring
	// já faz isso pra mim
	// é algo parecido com o Autowired pelo que eu entendi
	// e todas essas notações que tem o Bean, é essencial pra autenticação e etc,
	// mas eu não tenho 100% de controle delas, o Spring cuida disso ai
	
	
	//eu crio o "painel de controle"
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
		.headers(headers -> headers.disable()) // desabilita o frame pra acessar o h2-console
		
		.authorizeHttpRequests(auth -> auth
		.requestMatchers("/h2-console/**").permitAll()
		.requestMatchers("/admin-security/**").hasRole("ADMIN")
		.requestMatchers("/cliente-security/**").hasAnyRole("ADMIN", "CLIENTE").requestMatchers
		("/api/public/**", "/auth/**").permitAll().anyRequest().authenticated())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	} 
	//admin acessa tudo com o **
	//o Admin tbm pode acessar as rotas do cliente, mas só o admin pode acessar as dele
	//só usuários com a role admin podem acessar os requests que comecem com /api/admin, com rotas livres tbm
	//addFilterBefore eu uso pra adicionar o meu filtro personalizado 
	// sessionManagement não permite que o usuário fique logado pra sempre, então ele precisa do JWT toda vez

	@Bean // ele é tipo o chefe
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // retorna o manager que vai processar login/autenticação
	} // ele vai validar se o usuário existe e se a senha tá certa, já faz isso
		// automático, pq já é configurado pelo próprio String

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // é só um criptografador de senha padrão, mas é seguro tbm
	}

	
}
