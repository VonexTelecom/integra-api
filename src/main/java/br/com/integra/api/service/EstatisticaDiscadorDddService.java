package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.integra.api.repository.EstatisticaTotalizadorDddRepository;

@Service
public class EstatisticaDiscadorDddService {
	
	@Autowired
	private EstatisticaTotalizadorDddRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto>discadorTotalizadorDdd(EstatisticaFilter filter, Long clienteId){
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
		
		List<EstatisticaDiscadorOutputDto> chamadaBrutoTabela = new ArrayList<>();
		List<EstatisticaDiscadorOutputDto> chamadaProcessada = new ArrayList<>();
		
		
		LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		
		while(dataAtual.compareTo(dataFinalFormatada) <= 0) {
			String tipoEstatistica = String.format("chamadas_ddd");
			List<EstatisticaDiscador> chamadasDddBruto = new ArrayList<>();
			if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.build();
				
				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
			}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.build();
				
				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
			}else {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.build();
				
				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
			
			}

			for (int i = 11; i<=99; i++) {
					int a = i;
					if(dddInexistente(i) == true) {
						continue;
					}	
			EstatisticaDiscador estatistica =  chamadasDddBruto.stream().filter(chamada ->
				chamada.getTipoEstisticaValor().equals(String.valueOf(a)) && chamada.getTipoEstatistica().equals(tipoEstatistica) )
				.findFirst().orElseGet(() -> Optional.of(
						EstatisticaDiscador.builder()
							.tipoEstatistica(tipoEstatistica)
							.quantidade(BigDecimal.ZERO)
							.tipoEstisticaValor(String.valueOf(a))
							.build()).get());
			chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundoDDD(estatistica));
			
			}
			dataAtual = dataAtual.plusDays(1L);
		}
		chamadaProcessada.addAll(somaTabela(chamadaBrutoTabela));
		
		Long endTime = System.currentTimeMillis();
		System.out.printf("\ntempo de execução final %f", (float)(endTime - startTime)/1000);
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
