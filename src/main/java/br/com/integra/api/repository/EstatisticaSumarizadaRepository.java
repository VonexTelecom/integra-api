package br.com.integra.api.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.integra.api.model.EstatisticaSumarizada;

public interface EstatisticaSumarizadaRepository extends JpaRepository<EstatisticaSumarizada, Date>{

	@Query("from EstatisticaSumarizada d "
			+ "where d.data >= :dataInicial and "
			+ "d.data <= :dataFinal and d.clienteId = :clienteId")
	List<EstatisticaSumarizada> findByDatasPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, Long clienteId); 
}
 