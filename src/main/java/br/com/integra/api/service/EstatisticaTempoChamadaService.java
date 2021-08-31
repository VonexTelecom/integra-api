package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.internal.build.AllowSysOut;
import org.modelmapper.internal.bytebuddy.dynamic.scaffold.FieldLocator.ForExactType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaDiscadorRepository;

@Service
public class EstatisticaTempoChamadaService {
	
	@Autowired
	private EstatisticaDiscadorRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorTempoChamadas(EstatisticaFilter filter, Long cliente){
		LocalDate dataAtual;
		LocalDate dataFinalFormatada;
		LocalDate dataInicial = filter.getDataInicial().toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
		LocalDate dataFinal = filter.getDataFinal().toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
		
		
		
		if(filter.getPeriodoEnum() != null) {
			dataInicial = converterEnumToData(filter.getPeriodoEnum());
			dataFinal = dataInicial;
		}
		
		List<EstatisticaDiscadorOutputDto> chamadaBrutoTabela = new ArrayList<>();
		List<EstatisticaDiscadorOutputDto> chamadaProcessada = new ArrayList<>();
		List<EstatisticaDiscadorOutputDto> chamadaTabelaProcessada = new ArrayList<>();
		dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		
			System.out.println(dataFinalFormatada);
	
			dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			System.out.println(dataAtual);
			while(dataAtual.compareTo(dataFinalFormatada) <= 0) {
				String tipoEstatisticaOrigem = String.format("chamada_com_segundo_desc_origem");
				String tipoEstatisticaDestino = String.format("chamada_com_segundo_desc_destino");
				String tipoEstatisticaTotal = String.format("chamada_com_segundo_desc_total");
				
				
				List<EstatisticaDiscador> chamadasOrigemBruto =
						repository.findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaOrigem, filter,0,120);
				
				
				List<EstatisticaDiscador> chamadasDestinoBruto = 
						repository.findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaDestino, filter,0,120);
				
					int i;
					for (i = 0; i<=120; i++) {
						String nome = String.format("chamada_com_%d_segundo_desc_origem", i);
						int a = i;
						
						Optional<EstatisticaDiscador> estatisticaOrigem =  chamadasOrigemBruto.stream().filter(chamada ->
							chamada.getTipoEstisticaValor().equals(String.valueOf(a))  )
							.findFirst().or(() -> Optional.of(
									EstatisticaDiscador.builder()
										.tipoEstatistica(tipoEstatisticaOrigem)
										.quantidade(BigDecimal.ZERO)
										.tipoEstisticaValor(String.valueOf(a))
										.build()));
						
						System.out.println(chamadasOrigemBruto.size());
						chamadaProcessada.add(mapper.modelToOutputDtoSegundo(estatisticaOrigem.get()));
						
						Optional<EstatisticaDiscador> estatisticaDestino =  chamadasDestinoBruto.stream().filter(chamada ->
						Long.valueOf(chamada.getTipoEstisticaValor()) == a &&  chamada.getTipoEstatistica().equals(tipoEstatisticaDestino))
						.findFirst().or(() -> Optional.of(
								EstatisticaDiscador.builder()
									.tipoEstatistica(tipoEstatisticaDestino)
									.quantidade(BigDecimal.ZERO)
									.tipoEstisticaValor(String.valueOf(a))
									.build()));
					
						chamadaProcessada.add(mapper.modelToOutputDtoSegundo(estatisticaDestino.get()));
							
						}
					
					
				
				
				
				//chamadaProcessada.addAll(chamadasOrigemBruto);
				//chamadaProcessada.addAll(chamadasDestinoBruto);
				//chamadaBrutoTabela.add(chamadaTotal);	
				
				dataAtual = dataAtual.plusDays(1L);
				
			}
			//chamadaProcessada.addAll(estatisticaProcessada(chamadaBrutoTabela)); 
			//chamadaBrutoTabela.clear();
		
		
		return chamadaProcessada;
		
	}

	
	public List<EstatisticaDiscadorOutputDto> estatisticaProcessada(List<EstatisticaDiscadorOutputDto> listaBruta){
		
		String tipoEstatistica = "";
		
		List<EstatisticaDiscadorOutputDto> estatisticas = new ArrayList<>();
		
		
		for (EstatisticaDiscadorOutputDto estatistica : listaBruta) {
			tipoEstatistica = estatistica.getTipoEstatistica();
			new EstatisticaDiscadorOutputDto();
			EstatisticaDiscadorOutputDto estatisticaSumarizada = EstatisticaDiscadorOutputDto.builder()
				.tipoEstatistica(tipoEstatistica)
				.quantidade(quantidadeTotal(listaBruta))
				.build();
			estatisticas.add(estatisticaSumarizada);
		}
		
	
	return estatisticas;
	}
	
	
	public LocalDate converterEnumToData(PeriodoEstatisticaEnum periodoEnum) {
		
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

		return dataProcessada.toLocalDate();
		
	}
	
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
		
	
}
