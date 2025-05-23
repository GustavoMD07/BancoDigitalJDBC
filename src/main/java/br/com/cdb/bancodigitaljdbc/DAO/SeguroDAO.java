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
		String sql = "DELETE FROM seguro WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteAll(List<Seguro> seguros) {
		String sql = "DELETE FROM seguro WHERE id = ?";
		
		for(Seguro seguro : seguros ) {
			jdbcTemplate.update(sql, seguro.getId());
		}
	}
	
	
	public Optional<Seguro> findById(Long id) {
        String sql = "SELECT * FROM seguro WHERE id = ?";
        List<Seguro> lista = jdbcTemplate.query(sql, seguroRowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }
	
	public List<Seguro> findAll() {
        String sql = "SELECT * FROM seguro";
        return jdbcTemplate.query(sql, seguroRowMapper);
    }

    public List<Seguro> findByCartaoId(Long cartaoId) {
        String sql = "SELECT * FROM seguro WHERE cartao_id = ?";
        return jdbcTemplate.query(sql, seguroRowMapper, cartaoId);
    }
}
