package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.enums.TipoEstatisticaEnum;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.repository.EstatisticaTotalizadorChamadasRepository;

@Service
public class EstatisticaDiscadorChamadasService {
	
	@Autowired
	private EstatisticaTotalizadorChamadasRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorChamadas(EstatisticaFilter filter, Long clienteId) {
		
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
		
		
		if(filter.getDataInicial().after(filter.getDataFinal())) {
			throw new BusinessException("A data Inicial não pode ser maior que a final");
		}	
		
		if(filter.getPeriodoEnum() != null) {
			List<LocalDateTime> datas = converterEnumToData(filter.getPeriodoEnum());
			dataInicial = datas.get(0);
			dataFinal = datas.get(1);
			
		}else {
			 dataInicial = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 dataFinal = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 
		}
		
		System.out.println("DataInicial:"+dataInicial+" DataFinal:"+dataFinal);
		
		List<TipoEstatisticaEnum> list = Arrays.asList(TipoEstatisticaEnum.values());
		
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTabela = new ArrayList<>();

		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTotal = new ArrayList<>();
		
		
		for (TipoEstatisticaEnum tipoEstatisticaEnum : list) {
			EstatisticaDiscadorOutputDto totalizadorBrutoSumarizado = new EstatisticaDiscadorOutputDto();
			LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			List<EstatisticaDiscadorOutputDto> totalizadorBruto = new ArrayList<>();

			
			while(dataAtual.compareTo(dataFinalFormatada) <= 0){
				if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
					
					
				}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
					
					
					
				}else{
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
					
					
				}
					
					totalizadorBrutoSumarizado = EstatisticaDiscadorOutputDto.builder()
							.tipoEstatistica(tipoEstatisticaEnum.getValor())
							.quantidade(quantidadeTotal(totalizadorBruto)).build();
					
					totalizadorSumarizadoTabela.add(totalizadorBrutoSumarizado);
					
					dataAtual = dataAtual.plusDays(1L);
				}
			
		totalizadorBruto.clear();
		totalizadorSumarizadoTotal.add(estatisticaProcessada(totalizadorSumarizadoTabela));
		totalizadorSumarizadoTabela.clear();
			
		}

		Long endTime = System.currentTimeMillis();
		System.out.printf("\nduração: %f",(float)(endTime-startTime)/1000);
		
		return totalizadorSumarizadoTotal;
	}
	
	public EstatisticaDiscadorOutputDto estatisticaProcessada(List<EstatisticaDiscadorOutputDto> listaBruta){

		String tipoEstatistica = "";
		
		for (EstatisticaDiscadorOutputDto estatistica : listaBruta) {
			tipoEstatistica = estatistica.getTipoEstatistica();	
		}	
		
	EstatisticaDiscadorOutputDto estatisticaSumarizada = EstatisticaDiscadorOutputDto.builder()
			.tipoEstatistica(tipoEstatistica)
			.quantidade(quantidadeTotal(listaBruta))
			.build();
	
	return estatisticaSumarizada;
	}
	
	public List<LocalDateTime> converterEnumToData(PeriodoEstatisticaEnum periodoEnum) {
		
		LocalDateTime dataAtual = LocalDateTime.now();
		LocalDateTime dataProcessada = LocalDateTime.from(dataAtual);
		LocalDateTime dataFinalProcessada = LocalDateTime.from(dataProcessada);
		
		List<LocalDateTime> datas = new ArrayList<>();
		
		
		switch (periodoEnum) {
		case Hoje:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay();
			dataFinalProcessada = dataAtual.toLocalDate().atTime(23, 59);
			break;
		case Ontem:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusDays(1L);
			dataFinalProcessada =  dataProcessada.toLocalDate().atTime(23, 59);
			break;
		case QuinzeDias:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusWeeks(2).minusDays(1L);
			dataFinalProcessada =LocalDateTime.now().toLocalDate().atTime(23,59);
			break;
		case TrintaDias:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusMonths(1);
			dataFinalProcessada = LocalDateTime.now().toLocalDate().atTime(23,59);
			break;
		case OitoAsDezoito:
			dataProcessada = dataAtual.toLocalDate().atTime(8, 0, 0);
			dataFinalProcessada = dataAtual.toLocalDate().atTime(18, 0, 0);
			break;
		default:
		}
		datas.add(dataProcessada);
		datas.add(dataFinalProcessada);
		
		return datas;
		
	}
	
	
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public List<LocalDate> verificaIntervaloData(
			LocalDate startDate, LocalDate endDate) { 
		
		long dias = ChronoUnit.DAYS.between(startDate, endDate); 
		return IntStream.iterate(0, i -> i + 1)
				.limit(dias)
				.mapToObj(i -> startDate.plusDays(i))
				.collect(Collectors.toList()); 
	}
}
