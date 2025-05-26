package br.com.cdb.bancodigitaljdbc.DAO;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.cdb.bancodigitaljdbc.entity.Seguro;
import br.com.cdb.bancodigitaljdbc.rowMapper.SeguroRowMapper;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SeguroDAO {

	private final JdbcTemplate jdbcTemplate;
	private final SeguroRowMapper seguroRowMapper;
	
	public void save(Seguro seguro) {
		String sql = "CALL inserir_seguro_v1(?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, seguro.getNumeroApolice(), 
				seguro.getDataContratacao(), 
				seguro.getTipoDeSeguro(),
				seguro.getDescricao(), 
				seguro.getValorApolice(), 
				seguro.isAtivo(), 
				seguro.getCartao().getId());
	}
	
	public void delete (Long id) {
		String sql = "SELECT deletar_seguro_v1(?)";
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteAll(List<Seguro> seguros) {
		String sql = "SELECT deletar_todos_seguro_v1()";
		jdbcTemplate.execute(sql); //sem muito retorno
	}
	
	public Optional<Seguro> findById(Long id) {
        String sql = "SELECT * FROM encontrar_seguro_v1(?)";
        //aqui, eu retorno o objeto todo, então nessa function eu uso o returns table
        List<Seguro> lista = jdbcTemplate.query(sql, seguroRowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }
	
	public List<Seguro> findAll() {
        String sql = "SELECT * FROM encontrar_todos_seguros_v1()";
        return jdbcTemplate.query(sql, seguroRowMapper);
    }

    public List<Seguro> findByCartaoId(Long cartaoId) {
        String sql = "SELECT * FROM encontrar_seguro_por_cartao_v1";
        return jdbcTemplate.query(sql, seguroRowMapper, cartaoId);
    }
}
