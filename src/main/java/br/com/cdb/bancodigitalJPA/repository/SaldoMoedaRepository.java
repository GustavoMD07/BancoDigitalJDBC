package br.com.cdb.bancodigitalJPA.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.cdb.bancodigitalJPA.entity.SaldoMoeda;

public interface SaldoMoedaRepository extends JpaRepository<SaldoMoeda, Long> {
	Optional<SaldoMoeda> findByMoedaAndContaId(String moeda, Long id);
	
	List<SaldoMoeda> findByContaId(Long id);
}
