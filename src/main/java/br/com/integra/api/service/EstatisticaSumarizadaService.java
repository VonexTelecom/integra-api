package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.filter.EstatisticaFilterPeriodoData;
import br.com.integra.api.filter.EstatisticaFilterPeriodoMinuto;
import br.com.integra.api.mapper.EstatisticaSumarizadaMapper;
import br.com.integra.api.model.EstatisticaSumarizada;
import br.com.integra.api.repository.EstatisticaSumarizadaRepository;

@Service
public class EstatisticaSumarizadaService {
	
	@Autowired
	private EstatisticaSumarizadaRepository repository;
	
	@Autowired
	private EstatisticaSumarizadaMapper mapper;
	
	
	public EstatisticaSumarizadaOutputDto findPorPeriodo(EstatisticaFilterPeriodoMinuto filter, Long clienteId){
		
		LocalDateTime dataInicial = LocalDateTime.now();
		LocalDateTime dataFinal = converterEnumEmDataFinal(filter.getPeriodoEnum());
		List<EstatisticaSumarizada> estatisticas = 
				repository.findByDatasPeriodo(dataInicial, dataFinal, clienteId);
		EstatisticaSumarizada estatisticasPeriodo = EstatisticaSumarizada.builder()
				.chamadasCompletadas10Segundos(completadas10Segundos(estatisticas))
				.chamadasCompletadas30Segundos(completadas30Segundos(estatisticas))
				.chamadasCompletadas3Segundos(completadas3Segundos(estatisticas))
				.chamadasCompletadasMais30Segundos(completadasMais30Segundos(estatisticas))
				.chamadasCompletadas(completadas(estatisticas))
				.chamadasDiscadas(discadas(estatisticas))
				.clienteId(clienteId)
				.build();

		return mapper.modelToOutputDto(estatisticasPeriodo);
		
	}
	
	private LocalDateTime converterEnumEmDataFinal(PeriodoEstatisticaEnum periodoEnum) {
		
		LocalDateTime dataAtual = LocalDateTime.now();
		LocalDateTime dataProcessada = LocalDateTime.from(dataAtual);
		
		switch (periodoEnum) {
		case CincoMinutos:
			dataProcessada = dataAtual.minusMinutes(5L);
			break;
		case DezMinutos:
			dataProcessada = dataAtual.minusMinutes(10L);
			break;
		case TrintaMinutos:
			dataProcessada = dataAtual.minusMinutes(30L);
			break;
		default:
		}

		return dataProcessada;
	}
	

	public EstatisticaSumarizadaOutputDto findPorDatas(EstatisticaFilterPeriodoData filter, Long clienteId){
		
		
		List<EstatisticaSumarizada> estatisticas = 
				repository.findByDatasPeriodo(filter.getDataFinal().atTime(23,59,59),filter.getDataInicial().atStartOfDay(), clienteId);
	
		EstatisticaSumarizada estatisticasPeriodo = EstatisticaSumarizada.builder()
				.chamadasCompletadas10Segundos(completadas10Segundos(estatisticas))
				.chamadasCompletadas30Segundos(completadas30Segundos(estatisticas))
				.chamadasCompletadas3Segundos(completadas3Segundos(estatisticas))
				.chamadasCompletadasMais30Segundos(completadasMais30Segundos(estatisticas))
				.chamadasCompletadas(completadas(estatisticas))
				.chamadasDiscadas(discadas(estatisticas))
				.clienteId(clienteId)
				.build();

		return mapper.modelToOutputDto(estatisticasPeriodo);
		
	}

	private BigDecimal completadas10Segundos(List<EstatisticaSumarizada> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasCompletadas10Segundos())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal completadas30Segundos(List<EstatisticaSumarizada> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasCompletadas30Segundos())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal completadas3Segundos(List<EstatisticaSumarizada> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasCompletadas3Segundos())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal completadasMais30Segundos(List<EstatisticaSumarizada> estatisticas) {

		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasCompletadasMais30Segundos())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal completadas(List<EstatisticaSumarizada> estatisticas) {

		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasCompletadas())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal discadas(List<EstatisticaSumarizada> estatisticas) {
		
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getChamadasDiscadas())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
		
}
