package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.exception.EntidadeNaoEncontradaException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaSumarizadaMapper;
import br.com.integra.api.model.EstatisticaSumarizada;
import br.com.integra.api.repository.EstatisticaSumarizadaRepository;

@Service
public class EstatisticaSumarizadaService {
	
	@Autowired
	private EstatisticaSumarizadaRepository repository;
	
	@Autowired
	private EstatisticaSumarizadaMapper mapper;
	
	
	public EstatisticaSumarizadaOutputDto findPorPeriodo(EstatisticaFilter filter, Long clienteId){
		
		LocalDateTime dataInicial = LocalDateTime.now();
		LocalDateTime dataFinal = LocalDateTime.now();
		
		if(filter.getPeriodoEnum() != null) {
			dataInicial = converterEnumEmDataInicial(filter.getPeriodoEnum());
			dataFinal = converterEnumEmDataFinal(filter.getPeriodoEnum());			
		}else {
			if(dataInicial == null || dataFinal == null) {
				throw new EntidadeNaoEncontradaException("Data de Início e Data Final devem ser informadas!") {
					private static final long serialVersionUID = -5461664571083553002L;
				};
			}else {
				dataInicial = filter.getDataInicial().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();
				
				dataFinal = filter.getDataFinal().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();;
			}			
		}
		
		System.out.println(dataInicial+" ");
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
	
	private LocalDateTime converterEnumEmDataInicial(PeriodoEstatisticaEnum periodoEnum) {
		
		LocalDateTime dataAtual = LocalDateTime.now();
		LocalDateTime dataProcessada = LocalDateTime.from(dataAtual);
		
		switch (periodoEnum) {
		case Hoje:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay();
			break;
		case Ontem:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusDays(1);
			break;
		case QuinzeDias:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusWeeks(2);
			break;
		case TrintaDias:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusMonths(1);
			break;
		case OitoAsDezoito:
			dataProcessada = dataAtual.toLocalDate().atTime(8, 0, 0);
			break;
		default:
		}

		return dataProcessada;
	}

	private LocalDateTime converterEnumEmDataFinal(PeriodoEstatisticaEnum periodoEnum) {
		
		LocalDateTime dataAtual = LocalDateTime.now();
		LocalDateTime dataProcessada = LocalDateTime.from(dataAtual);
		
		switch (periodoEnum) {
		case Hoje:
			dataProcessada = dataAtual.toLocalDate().atTime(23, 59, 59);
			break;
		case Ontem:
			dataProcessada = dataAtual.toLocalDate().atTime(23, 59, 59).minusDays(1);
			break;
		case QuinzeDias:
			dataProcessada = dataAtual.toLocalDate().atTime(23, 59, 59);
			break;
		case TrintaDias:
			dataProcessada = dataAtual.toLocalDate().atTime(23, 59, 59);
			break;
		case OitoAsDezoito:
			dataProcessada = dataAtual.toLocalDate().atTime(18, 0, 0);
			break;
		default:
		}

		return dataProcessada;
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
