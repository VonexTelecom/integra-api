package br.com.integra.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.integra.api.model.EstatisticaSumarizadas;

public interface EstatisticaSumarizadaRepository extends JpaRepository<EstatisticaSumarizadas, Long>, JpaSpecificationExecutor<EstatisticaSumarizadas>{

}
