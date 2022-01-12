package com.condominio.contas.dao;

import java.math.BigDecimal;
import java.util.List;

import com.condominio.contas.domain.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface ContaDao extends PagingAndSortingRepository<Conta, Long> {

	Page<Conta> findAllByIdCondominioOrderBySiglaAsc(Long condominio, Pageable pagina);

	Boolean existsBySiglaAndIdCondominio(String sigla, Long condominio);

	Boolean existsBySiglaAndIdCondominioAndIdContaNot(String sigla, Long condominio, Long idConta);

	@Query("select sum(saldoAtual) from #{#entityName} c where c.idCondominio = :condominio")
	BigDecimal sumSaldoAtualByCondominio(@Param("condominio") Long condominio);

	List<Conta> findByIdCondominio(Long condominio);

}
