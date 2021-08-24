package br.com.integra.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.dto.output.EstatisticaSumarizadaPeriodoOutputDto;
import br.com.integra.api.filter.EstatisticaFilterPeriodoData;
import br.com.integra.api.filter.EstatisticaFilterPeriodoMinuto;
import br.com.integra.api.mapper.EstatisticaSumarizadaMapper;
import br.com.integra.api.model.EstatisticaSumarizadas;
import br.com.integra.api.repository.EstatisticaSumarizadaRepository;
import br.com.integra.api.repository.specification.EstatisticaSumarizadaSpecification;

@Service
public class EstatisticaSumarizadaService {
	
	@Autowired
	private EstatisticaSumarizadaRepository repository;
	
	@Autowired
	private EstatisticaSumarizadaMapper mapper;
	
	
	public Page<EstatisticaSumarizadaOutputDto> findAll(EstatisticaSumarizadaSpecification spec, Pageable pageable) {	
		Page<EstatisticaSumarizadas> page =  repository.findAll(spec, pageable);	
		return page.map(user -> mapper.modelToOutputDto(user));
	}
	

	public EstatisticaSumarizadaPeriodoOutputDto FindAllPeriodo(EstatisticaSumarizadaSpecification spec, EstatisticaFilterPeriodoMinuto filter){
		
		List<EstatisticaSumarizadas> estatisticas = repository.findAll(spec);
		
		EstatisticaSumarizadas estatisticasPeriodo = new EstatisticaSumarizadas().builder()
				.chamadasComplementadas10Segundos(BigDecimal.ZERO)
				.chamadasComplementadas30Segundos(BigDecimal.ZERO)
				.chamadasComplementadas3Segundos(BigDecimal.ZERO)
				.chamadasComplementadasMais30Segundos(BigDecimal.ZERO)
				.chamadasCompletadas(BigDecimal.ZERO)
				.chamadasDiscadas(BigDecimal.ZERO)
				.clienteId(filter.getCliente())
				.build();

		Long valor = filter.getPeriodoEnum().getIndex();
		
		for (EstatisticaSumarizadas estatisticaSumarizada : estatisticas) {
			if (valor > 0) {
				 
				estatisticasPeriodo.setChamadasDiscadas(estatisticasPeriodo.getChamadasDiscadas().add(estatisticaSumarizada.getChamadasDiscadas()));
				estatisticasPeriodo.setChamadasCompletadas(estatisticasPeriodo.getChamadasCompletadas().add(estatisticaSumarizada.getChamadasCompletadas()));
				estatisticasPeriodo.setChamadasComplementadas10Segundos(estatisticasPeriodo.getChamadasComplementadas10Segundos().add(estatisticaSumarizada.getChamadasComplementadas10Segundos()));
				estatisticasPeriodo.setChamadasComplementadas30Segundos(estatisticasPeriodo.getChamadasComplementadas30Segundos().add(estatisticaSumarizada.getChamadasComplementadas30Segundos()));
				estatisticasPeriodo.setChamadasComplementadas3Segundos(estatisticasPeriodo.getChamadasComplementadas3Segundos().add(estatisticaSumarizada.getChamadasComplementadas3Segundos()));
				estatisticasPeriodo.setChamadasComplementadasMais30Segundos(estatisticasPeriodo.getChamadasComplementadasMais30Segundos().add(estatisticaSumarizada.getChamadasComplementadasMais30Segundos()));
				
				valor--;
			}
		}
		
		return mapper.periodoModelToOutputDto(estatisticasPeriodo);
		
	}
	
	public EstatisticaSumarizadaPeriodoOutputDto FindAllPeridoData(EstatisticaSumarizadaSpecification spec, EstatisticaFilterPeriodoData filter){
		
		
		List<EstatisticaSumarizadas> estatisticas = repository.findByDatesPeriodos(filter.getDataInicial(), filter.getDataFinal(), filter.getCliente());
		
		
		
		EstatisticaSumarizadas estatisticasPeriodo = new EstatisticaSumarizadas().builder()
				.chamadasComplementadas10Segundos(BigDecimal.ZERO)
				.chamadasComplementadas30Segundos(BigDecimal.ZERO)
				.chamadasComplementadas3Segundos(BigDecimal.ZERO)
				.chamadasComplementadasMais30Segundos(BigDecimal.ZERO)
				.chamadasCompletadas(BigDecimal.ZERO)
				.chamadasDiscadas(BigDecimal.ZERO)
				.clienteId(filter.getCliente())
				.build();

		
		for (EstatisticaSumarizadas estatisticaSumarizada : estatisticas) { 
				estatisticasPeriodo.setChamadasDiscadas(estatisticasPeriodo.getChamadasDiscadas().add(estatisticaSumarizada.getChamadasDiscadas()));
				estatisticasPeriodo.setChamadasCompletadas(estatisticasPeriodo.getChamadasCompletadas().add(estatisticaSumarizada.getChamadasCompletadas()));
				estatisticasPeriodo.setChamadasComplementadas10Segundos(estatisticasPeriodo.getChamadasComplementadas10Segundos().add(estatisticaSumarizada.getChamadasComplementadas10Segundos()));
				estatisticasPeriodo.setChamadasComplementadas30Segundos(estatisticasPeriodo.getChamadasComplementadas30Segundos().add(estatisticaSumarizada.getChamadasComplementadas30Segundos()));
				estatisticasPeriodo.setChamadasComplementadas3Segundos(estatisticasPeriodo.getChamadasComplementadas3Segundos().add(estatisticaSumarizada.getChamadasComplementadas3Segundos()));
				estatisticasPeriodo.setChamadasComplementadasMais30Segundos(estatisticasPeriodo.getChamadasComplementadasMais30Segundos().add(estatisticaSumarizada.getChamadasComplementadasMais30Segundos()));
		}
		
		return mapper.periodoModelToOutputDto(estatisticasPeriodo);
		
	}
	
		
}
