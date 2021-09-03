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

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaTotalizadorTempoRepository;

@Service
public class EstatisticaTempoChamadaService {
	
	@Autowired
	private EstatisticaTotalizadorTempoRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorTempoChamadas(EstatisticaFilter filter, Long clienteId){

		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
	
		if(filter.getPeriodoEnum() != null) {
			List<LocalDateTime> datas = converterEnumToData(filter.getPeriodoEnum());
			dataInicial = datas.get(0);
			dataFinal = datas.get(1);
			
		}else {
			 dataInicial = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 dataFinal = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
		
		List<EstatisticaDiscadorOutputDto> chamadaBrutoTabela = new ArrayList<>();
		List<EstatisticaDiscadorOutputDto> chamadaProcessada = new ArrayList<>();
		List<EstatisticaDiscadorOutputDto> chamadaTabelaProcessada = new ArrayList<>();
			
				
			
		LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		while(dataAtual.compareTo(dataFinalFormatada) <= 0) {
			List<EstatisticaDiscador> chamadasOrigemBruto = new ArrayList<>();
			List<EstatisticaDiscador> chamadasDestinoBruto = new ArrayList<>();
			String tipoEstatisticaOrigem = String.format("chamada_com_segundo_desc_origem");
			String tipoEstatisticaDestino = String.format("chamada_com_segundo_desc_destino");
			String tipoEstatisticaTotal = String.format("chamada_com_segundo_desc_total");
			
		
			
			if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.build();
				
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));

			}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
				
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.build();
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));
				
			}else {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
						.build();
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));
			}
			
			for (int i = 0; i<=120; i++) {
				int a = i;
						
					EstatisticaDiscador estatisticaOrigem =  chamadasOrigemBruto.stream().filter(chamada ->
						chamada.getTipoEstisticaValor().equals(String.valueOf(a)) && chamada.getTipoEstatistica().equals(tipoEstatisticaOrigem) )
						.findFirst().orElseGet(() -> Optional.of(
								EstatisticaDiscador.builder()
									.tipoEstatistica(tipoEstatisticaOrigem)
									.quantidade(BigDecimal.ZERO)
									.tipoEstisticaValor(String.valueOf(a))
									.build()).get());
						
						
					EstatisticaDiscador estatisticaDestino =  chamadasDestinoBruto.stream().filter(chamada ->
					chamada.getTipoEstisticaValor().equals(String.valueOf(a))  &&  chamada.getTipoEstatistica().equals(tipoEstatisticaDestino))
					.findFirst().orElseGet(() -> Optional.of(
							EstatisticaDiscador.builder()
								.tipoEstatistica(tipoEstatisticaDestino)
								.quantidade(BigDecimal.ZERO)
								.tipoEstisticaValor(String.valueOf(a))
								.build()).get());
				
					
					
					EstatisticaDiscador estatisticaTotal = EstatisticaDiscador.builder()
							.quantidade(estatisticaDestino.getQuantidade().add(estatisticaOrigem.getQuantidade()))
							.tipoEstatistica(tipoEstatisticaTotal)
							.tipoEstisticaValor(String.valueOf(a)).build();
					
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaOrigem));
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaDestino));
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaTotal));
					 
				}
			
			
			chamadaTabelaProcessada.addAll(somaTabela(chamadaBrutoTabela));
			
			chamadaBrutoTabela.clear();
			
			dataAtual = dataAtual.plusDays(1L);	
		}
		
		chamadaProcessada.addAll(somaTabela(chamadaTabelaProcessada));
		return chamadaProcessada;
		
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
	
	public List<EstatisticaDiscadorOutputDto> somaTabela(List<EstatisticaDiscadorOutputDto> list){
		List<EstatisticaDiscadorOutputDto> estatisticaProcesada = new ArrayList<>();
		
		for(int i = 0; i<=120; i++) {
			String tipoEstatisticaOrigem = String.format("chamada_com_%d_segundo_desc_origem", i);
			String tipoEstatisticaDestino = String.format("chamada_com_%d_segundo_desc_destino", i);
			String tipoEstatisticaTotal = String.format("chamada_com_%d_segundo_desc_total", i);
			
			List<EstatisticaDiscadorOutputDto> estatisticasDestino =  list.stream().filter(chamada ->
			chamada.getTipoEstatistica().equals(tipoEstatisticaDestino)).collect(Collectors.toList());
			
			List<EstatisticaDiscadorOutputDto> estatisticasOrigem =  list.stream().filter(chamada ->
			chamada.getTipoEstatistica().equals(tipoEstatisticaOrigem)).collect(Collectors.toList());
			
			List<EstatisticaDiscadorOutputDto> estatisticasTotal =  list.stream().filter(chamada ->
			chamada.getTipoEstatistica().equals(tipoEstatisticaTotal)).collect(Collectors.toList());
			
			EstatisticaDiscadorOutputDto estatisticaDestinoSumarizador = EstatisticaDiscadorOutputDto.builder()
					.tipoEstatistica(tipoEstatisticaDestino)
					.quantidade(quantidadeTotal(estatisticasDestino)).build();
			
			EstatisticaDiscadorOutputDto estatisticaOrigemSumarizador = EstatisticaDiscadorOutputDto.builder()
					.tipoEstatistica(tipoEstatisticaOrigem)
					.quantidade(quantidadeTotal(estatisticasOrigem)).build();
			
			EstatisticaDiscadorOutputDto estatisticaTotalSumarizador = EstatisticaDiscadorOutputDto.builder()
					.tipoEstatistica(tipoEstatisticaTotal)
					.quantidade(quantidadeTotal(estatisticasTotal)).build();
			
			estatisticaProcesada.add(estatisticaOrigemSumarizador);
			estatisticaProcesada.add(estatisticaDestinoSumarizador);
			estatisticaProcesada.add(estatisticaTotalSumarizador);
			
			estatisticasDestino.clear();
			estatisticasOrigem.clear();
			estatisticasTotal.clear();
		}
		
		return estatisticaProcesada;
	}
	
	
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	
}
