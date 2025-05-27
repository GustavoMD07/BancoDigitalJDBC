package br.com.cdb.bancodigitaljdbc.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.entity.Cartao;
import br.com.cdb.bancodigitaljdbc.entity.CartaoCredito;
import br.com.cdb.bancodigitaljdbc.entity.CartaoDebito;
import br.com.cdb.bancodigitaljdbc.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitaljdbc.rowMapper.CartaoRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CartaoDAO {

	private final JdbcTemplate jdbcTemplate;
	private final CartaoRowMapper cartaoRowMapper;
	private final ContaDAO contaDAO;

	public void save(Cartao cartao) {
		String sql = "SELECT inserir_cartao_v2 (?, ?, ?, ?, ?, ?, ?, ?)";

		Object fatura = null, limiteDiario = null, limiteCredito = null;
		// tava bugando, erro de não ser inicializado
		if (cartao instanceof CartaoCredito) {
			CartaoCredito cc = (CartaoCredito) cartao;
			limiteCredito = cc.getLimiteCredito();
			fatura = cc.getFatura();
		} else if (cartao instanceof CartaoDebito) {
			CartaoDebito cd = (CartaoDebito) cartao;
			limiteDiario = cd.getLimiteDiario();

		} else {
			throw new SubClasseDiferenteException("Tipo de cartão inválido: " + cartao.getTipoDeCartao());
		}

		Long id = jdbcTemplate.queryForObject(sql, Long.class,
				cartao.getSenha(),cartao.isStatus(),
				cartao.getNumCartao(),cartao.getTipoDeCartao(),
				cartao.getConta().getId(), fatura,
				limiteCredito, limiteDiario
				);
		cartao.setId(id);
	}

	public void delete(Long id) {
		String sql = "SELECT deletar_cartao_v1(?)";
		jdbcTemplate.update(sql, id);
	}

	public void update(Cartao cartao) {
		String sql = "SELECT atualizar_cartao_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Object fatura = null;
		Object limiteCredito = null;
		Object limiteDebito = null;

		if (cartao instanceof CartaoCredito) {
			CartaoCredito cc = (CartaoCredito) cartao;
			fatura = cc.getFatura();
			limiteCredito = cc.getLimiteCredito();
		} else if (cartao instanceof CartaoDebito) {
			CartaoDebito cd = (CartaoDebito) cartao;
			limiteDebito = cd.getLimiteDiario(); // ou getLimiteDebito(), conforme seu getter
		}
		jdbcTemplate.update(sql,cartao.getId(), cartao.getSenha(), cartao.isStatus(), cartao.getNumCartao(), cartao.getTipoDeCartao(),  
				cartao.getConta().getId(), fatura, limiteCredito, limiteDebito );
	}

	public void deleteAll() {
		String sql = "SELECT deletar_todos_cartao_v1()";
		jdbcTemplate.execute(sql);
	} //function pra "resetar" o banco de dados

	public Optional<Cartao> findById(Long id) {
		String sql = "SELECT * FROM encontrar_cartao_v1(?)";
		List<Cartao> lista = jdbcTemplate.query(sql, cartaoRowMapper, id);
		if (lista.isEmpty())
			return Optional.empty();

		Cartao cartao = lista.get(0); // pega o primeiro (e único) que achar com o id
        contaDAO.findById(cartao.getContaId()).ifPresent(cartao::setConta);;
        
        
        return Optional.of(cartao);
	}
	// queryForObject só me retorna um resultado, o query retorna uma lista
	// aqui ele retorna só o primeiro do indíce caso ele ache, por isso o uso do
	// Optional

	public List<Cartao> findAll() {
		String sql = "SELECT * FROM encontrar_todos_cartao_v1()";

		return jdbcTemplate.query(sql, cartaoRowMapper);
	}

	public List<Cartao> findByContaId(Long contaId) {
		String sql = "SELECT * FROM encontrar_cartao_por_conta_v1(?)";
		return jdbcTemplate.query(sql, cartaoRowMapper, contaId);
	}

}
