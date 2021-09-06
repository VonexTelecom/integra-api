package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaCapsMapper;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaCapsRepository;
import br.com.integra.api.utils.DateUtils;

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
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
	
		if((filter.getDataInicial()!=null && filter.getDataFinal()!=null) && filter.getDataInicial().after(filter.getDataFinal())) {
			throw new BusinessException("A data Inicial não pode ser maior que a final");
		}	
		
		else if(filter.getPeriodoEnum() != null) {
			List<LocalDateTime> datas = DateUtils.converterEnumToData(filter.getPeriodoEnum());
			dataInicial = datas.get(0);
			dataFinal = datas.get(1);
			
		}else if(filter.getDataInicial()!=null && filter.getDataFinal()!=null) {
			 dataInicial = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 dataFinal = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 
		}else {
			throw new BusinessException("Selecione um periodo ou uma data incial e final.");
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
				
				
				capsProcessado.addAll(separadorCaps(capsBruto, dataInicial, dataFinal));
				dataAtual = dataAtual.plusDays(1L);
			}
			if(!capsProcessado.stream().filter(t -> t.getValores().stream().filter(v -> v.getQuantidade().longValue() > 0).findAny().isPresent()).findAny().isPresent()) {
				return new ArrayList<>();
			}
		return capsProcessado;
	}
	public List<EstatisticaCapsOutputDto> separadorCaps(List<EstatisticaDiscador> lista, LocalDateTime dataInicial, LocalDateTime dataFinal){
		
		List<EstatisticaCapsOutputDto> capsProcessado = new ArrayList<>();
		while (dataInicial.compareTo(dataFinal) <= 0) {
			LocalDateTime dataFim = dataInicial.plusMinutes(1L);
			LocalDateTime dataIni = dataInicial;
			
			List<EstatisticaDiscadorOutputDto> caps = estatisticaMapper.modelToCollectionOutputDto(lista.stream().filter
					(c -> c.getData().compareTo (dataIni.toLocalTime()) == 0 &&
					c.getData().compareTo(dataFim.toLocalTime()) <= 0).collect(Collectors.toList()));
			


			capsProcessado.add(mapper.modelToOutputDto(caps,dataInicial));
			dataInicial = dataInicial.plusMinutes(1L);
		}
		return capsProcessado;
	}
	
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
