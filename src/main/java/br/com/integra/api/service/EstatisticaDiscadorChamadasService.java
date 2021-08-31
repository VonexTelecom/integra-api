package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import br.com.integra.api.enums.TipoEstatisticaEnum;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.repository.EstatisticaDiscadorRepository;

@Service
public class EstatisticaDiscadorChamadasService {
	
	@Autowired
	private EstatisticaDiscadorRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorChamadas(EstatisticaFilter filter, Long clienteId) {
		LocalDate dataAtual;
		LocalDate dataFinalFormatada;
		LocalDate dataInicial = filter.getDataInicial().toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
		LocalDate dataFinal = filter.getDataFinal().toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
	
		
		if(filter.getPeriodoEnum() != null) {
			dataInicial = converterEnumToData(filter.getPeriodoEnum());
			dataFinal = dataInicial;
		}
		
		List<TipoEstatisticaEnum> list = Arrays.asList(TipoEstatisticaEnum.values());
		
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTabela = new ArrayList<>();

		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTotal = new ArrayList<>();
		
		for (TipoEstatisticaEnum tipoEstatisticaEnum : list) {
			EstatisticaDiscadorOutputDto totalizadorBrutoSumarizado = new EstatisticaDiscadorOutputDto();
			
			dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			
			
			dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			
			while(dataAtual.compareTo(dataFinalFormatada) <= 0){
				
					List<EstatisticaDiscadorOutputDto> totalizadorBruto = (mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaEnum.getValor(),filter)));
					
					
					totalizadorBrutoSumarizado = EstatisticaDiscadorOutputDto.builder()
							.tipoEstatistica(tipoEstatisticaEnum.getValor())
							.quantidade(quantidadeTotal(totalizadorBruto)).build();
					
					totalizadorSumarizadoTabela.add(totalizadorBrutoSumarizado);
					
					dataAtual = dataAtual.plusDays(1L);
				}
		totalizadorSumarizadoTotal.add(estatisticaProcessada(totalizadorSumarizadoTabela));
		totalizadorSumarizadoTabela.clear();
			
		}
		
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
