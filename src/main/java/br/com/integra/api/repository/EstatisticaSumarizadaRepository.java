package br.com.integra.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.integra.api.model.EstatisticaSumarizadas;

public interface EstatisticaSumarizadaRepository extends JpaRepository<EstatisticaSumarizadas, Date>, JpaSpecificationExecutor<EstatisticaSumarizadas>{

	@Query("from EstatisticaSumarizadas d where d.data >= :dataInicial and d.data <= :dataFinal and d.clienteId = :clienteId")
	List<EstatisticaSumarizadas> findByDatesPeriodos(
			@Param("dataInicial") Date dataInicial , @Param("dataFinal") Date dataFinal,
			@Param("clienteId") Long clienteId);
}
 