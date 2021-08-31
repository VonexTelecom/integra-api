package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.integra.api.repository.EstatisticaDiscadorRepository;

@Service
public class EstatisticaDiscadorDdd {
	
	@Autowired
	private EstatisticaDiscadorRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto>discadorTotalizadorDdd(EstatisticaFilter filter, Long clienteId){
		Long startTime = System.currentTimeMillis();
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
		
		
		dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
	
		dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		
		while(dataAtual.compareTo(dataFinalFormatada) <= 0) {
			String tipoEstatistica = String.format("chamadas_ddd");
				
			List<EstatisticaDiscador> chamadasDddBruto =
					repository.findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatistica, filter,clienteId, 11,99);
			

			for (int i = 11; i<=99; i++) {
					int a = i;
					if(dddInexistente(i) == true) {
						continue;
					}
					
			EstatisticaDiscador estatistica =  chamadasDddBruto.stream().filter(chamada ->
				chamada.getTipoEstisticaValor().equals(String.valueOf(a)) && chamada.getTipoEstatistica().equals(tipoEstatistica) )
				.findFirst().or(() -> Optional.of(
						EstatisticaDiscador.builder()
							.tipoEstatistica(tipoEstatistica)
							.quantidade(BigDecimal.ZERO)
							.tipoEstisticaValor(String.valueOf(a))
							.build())).get();
			chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundoDDD(estatistica));
			
			}
			dataAtual = dataAtual.plusDays(1L);
		}
		chamadaProcessada.addAll(somaTabela(chamadaBrutoTabela));
		
		Long endTime = System.currentTimeMillis();
		//System.out.printf("\ntempo de execução final %f", (float)(endTime - startTime)/1000);
		return chamadaProcessada;
	}
	
	
	public List<EstatisticaDiscadorOutputDto> somaTabela(List<EstatisticaDiscadorOutputDto> lista){
		List<EstatisticaDiscadorOutputDto> estatisticasProcessadas = new ArrayList<>();
		
		for (int i = 11; i<=99; i++) {
			if(dddInexistente(i) == true) {
				continue;
			}
			String nome = String.format("chamadas_ddd_%d", i);
			
			List<EstatisticaDiscadorOutputDto> estatisticaDdd =  lista.stream().filter(chamada ->
			chamada.getTipoEstatistica().equals(nome)).collect(Collectors.toList());
			
			EstatisticaDiscadorOutputDto estatisticaSumarizador = EstatisticaDiscadorOutputDto.builder()
					.tipoEstatistica(nome)
					.quantidade(quantidadeTotal(estatisticaDdd)).build();
			
			estatisticasProcessadas.add(estatisticaSumarizador);
		}
		return estatisticasProcessadas;
		
	}
	
	
	
	
	
	public boolean dddInexistente(int ddd) {
		List<Integer> dddInvalidos = Arrays.asList(20, 23, 25, 26, 29, 30, 36, 39
				, 40, 50, 52, 56, 57, 58, 59, 60, 70, 72, 76, 78, 80, 90);
		boolean dddInvalido = dddInvalidos.stream().anyMatch(valor -> valor == ddd);
		return dddInvalido;
		
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
