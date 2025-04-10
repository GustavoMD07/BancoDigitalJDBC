package br.com.cdb.bancodigitalJPA.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.cdb.bancodigitalJPA.security.service.UsuarioService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//filtro = basicamente o portão de segurança antes de chegar no meu controller
//ele é o filtro que vai interceptar as requisições, e validar o token JWT, toda vez que ocorre alguma
//requisição Https, ele vê se antes da ação realizar, o token é válido
//basicamente, ele age antes de qualquer requisição, tipo um segurança na porta de uma festa
// ele extrai o email do usuário e deixa ele "Passar". 

@Component // component pro String gerenciar a classe pra mim, e então ela fica disponível
@RequiredArgsConstructor // pra eu injetar com o RequiredArgs ou Autowired
public class JwtAuthFilter extends OncePerRequestFilter {
//o RequiredArgsConstructor já gera o construtor automático com os atributos finais

	private final JwtService jwtService;
	private final UsuarioService usuarioService;

	@Override // esse método vai ser chamado toda vez que uma requisição for feita
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// aqui eu vou pegar o token do header da requisição
		 String authHeader = request.getHeader("Authorization"); // pega o cabeçalho de autorização

		// o cabeçalho é responsável por mandar informações pro servidor/memoria
		// verifico se o cabeçalho não é nulo e começa com Bearer

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response); // deixa passar a requisição pro próximo filtro ou pro controller
			return; // se não tiver o token, não precisa continuar
		}

		String jwt = authHeader.substring(7); // removo o Bearer e um espaço (7 letras)
		String email = jwtService.extrairUsername(jwt); // pego o email do token

		// aqui é o processo pra validar o token
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = usuarioService.loadUserByUsername(email); // carrega o usuário do banco de dados

			if (jwtService.tokenValido(jwt, userDetails.getUsername())) { // verifica se o token é válido
				// aqui eu crio a autenticação do usuário
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities()); // cria o token de autenticação
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response); // deixa passar a requisição pro próximo filtro ou pro controller
	}

}
