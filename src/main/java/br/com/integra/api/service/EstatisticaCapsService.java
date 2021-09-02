package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaCapsMapper;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaCapsRepository;
import ch.qos.logback.core.net.SyslogOutputStream;

@Service
public class EstatisticaCapsService {
	
	@Autowired
	private EstatisticaCapsRepository repository;
	
	@Autowired
	private EstatisticaCapsMapper mapper;
	
	@Autowired
	private EstatisticaDiscadorMapper estatisticaMapper;
	
	public List<EstatisticaCapsOutputDto> discadorTotalizadorCaps(EstatisticaFilter filter, Long clienteId) {
		Long startTime = System.currentTimeMillis();
		LocalDateTime dataInicial = filter.getDataInicial().toInstant().atZone( ZoneId.systemDefault() ).toLocalDateTime();
		LocalDateTime dataFinal = filter.getDataFinal().toInstant().atZone( ZoneId.systemDefault() ).toLocalDateTime();
		
		
		
		if(filter.getPeriodoEnum() != null) {
			LocalDateTime dataInicialEnum = null; 
			LocalDateTime dataFinalEnum = null;
			List<LocalDateTime> datas = converterEnumToData(filter.getPeriodoEnum());
			for (LocalDateTime localDateTime : datas) {
				if(dataInicialEnum == null) {
					dataInicialEnum = localDateTime;
				}else{
					dataFinalEnum = localDateTime;
				}
				
				dataInicial = dataInicialEnum;
				dataFinal = dataFinalEnum;
			}
		}
		
			List<EstatisticaCapsOutputDto> capsProcessado = new ArrayList<>();
			
			
			LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			EstatisticaFilter filtro = new EstatisticaFilter();
			
			while(dataAtual.compareTo(dataFinalFormatada) <= 0) {
				
				List<EstatisticaDiscador> capsBruto = new ArrayList<>();
				if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
					filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
					
					capsBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, filtro,clienteId));
					
				}else  if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					System.out.println(dataAtual);
					filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.build();
					
					capsBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, filtro,clienteId));
					
				}else {
					filtro = EstatisticaFilter.builder()
							.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
		
					
					capsBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, filtro,clienteId));
	
				}
				System.out.print(capsBruto);
				capsProcessado.addAll(separadorCaps(capsBruto, dataInicial, dataFinal));
				capsBruto.clear();
				dataAtual = dataAtual.plusDays(1L);
			}
			
			
			
		
		return capsProcessado;
	}
	
	public List<EstatisticaCapsOutputDto> separadorCaps(List<EstatisticaDiscador> lista, LocalDateTime dataInicial, LocalDateTime dataFinal){
		
		List<EstatisticaCapsOutputDto> capsProcessado = new ArrayList<>();
		LocalDateTime dataInicialModificado = dataInicial;
		while (dataInicialModificado.compareTo(dataFinal) <=0) {
			LocalDateTime dataIni = dataInicialModificado;
			LocalDateTime dataFim = dataIni.plusMinutes(1L);
			
			
			List<EstatisticaDiscadorOutputDto> caps = estatisticaMapper.modelToCollectionOutputDto(lista.stream().filter
					(c -> c.getData().compareTo(Date.from(dataIni.atZone(ZoneId.systemDefault()).toInstant())) == 0 && 
					c.getData().compareTo(Date.from(dataFim.atZone(ZoneId.systemDefault()).toInstant())) <= 0
					).collect(Collectors.toList()));
			
			capsProcessado.add(mapper.modelToOutputDto(caps,dataInicial));
			dataInicialModificado = dataInicial.plusMinutes(1L);
			//System.out.println(caps.get(0) +"\n"+ caps.get(1));
			
		}
		return capsProcessado;
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
}
