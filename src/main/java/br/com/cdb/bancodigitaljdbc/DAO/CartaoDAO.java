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
		String sql = "CALL inserir_cartao_v1 (?, ?, ?, ?, ?, ?, ?, ?)";

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

		jdbcTemplate.update(sql, cartao.getSenha(), cartao.isStatus(), cartao.getNumCartao(), cartao.getTipoDeCartao(),
				cartao.getConta().getId(), fatura, limiteCredito, limiteDiario);
	}

	public void delete(Long id) {
		String sql = "DELETE FROM cartao WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	public void update(Cartao cartao) {
		String sql = """
				UPDATE cartao
				   SET tipo_de_cartao  = ?,
				       num_cartao       = ?,
				       senha            = ?,
				       status           = ?,
				       conta_id         = ?,
				       fatura           = ?,
				       limite_credito   = ?,
				       limite_diario    = ?
				 WHERE id = ?
				""";

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
		jdbcTemplate.update(sql, cartao.getTipoDeCartao(), cartao.getNumCartao(), cartao.getSenha(), cartao.isStatus(),
				cartao.getConta().getId(), fatura, limiteCredito, limiteDebito, cartao.getId());
	}

	public void deleteAll(List<Cartao> cartoes) {

		String sql = "DELETE FROM cartao WHERE id = ?";

		for (Cartao cartao : cartoes) {
			jdbcTemplate.update(sql, cartao.getId());
		}
	}

	public Optional<Cartao> findById(Long id) {
		String sql = "SELECT * FROM cartao WHERE id = ?";
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
		String sql = "SELECT * FROM cartao";

		return jdbcTemplate.query(sql, cartaoRowMapper);
	}

	public List<Cartao> findByContaId(Long contaId) {
		String sql = "SELECT * FROM cartao WHERE conta_id = ?";
		return jdbcTemplate.query(sql, cartaoRowMapper, contaId);
	}

}
