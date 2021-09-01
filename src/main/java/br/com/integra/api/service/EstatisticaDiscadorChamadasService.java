package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.oauth2.sdk.util.date.DateWithTimeZoneOffset;

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
		
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime dataFinal = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	
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
		System.out.println("DataInicial:"+dataInicial+" DataFinal:"+dataFinal);
		
		List<TipoEstatisticaEnum> list = Arrays.asList(TipoEstatisticaEnum.values());
		
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTabela = new ArrayList<>();

		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTotal = new ArrayList<>();
		
		
		for (TipoEstatisticaEnum tipoEstatisticaEnum : list) {
			EstatisticaDiscadorOutputDto totalizadorBrutoSumarizado = new EstatisticaDiscadorOutputDto();
			LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getDayOfMonth(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getDayOfMonth(), dataFinal.getDayOfMonth());
			
			while(dataAtual.compareTo(dataFinalFormatada) <= 0){
				if(dataAtual.compareTo(dataFinalFormatada) < 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.dataFinal(null)
							.build();
					
					List<EstatisticaDiscadorOutputDto> totalizadorBruto = (mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaEnum.getValor(),filter, clienteId)));
					
					totalizadorBrutoSumarizado = EstatisticaDiscadorOutputDto.builder()
							.tipoEstatistica(tipoEstatisticaEnum.getValor())
							.quantidade(quantidadeTotal(totalizadorBruto)).build();
				}else{
					if(dataAtual.compareTo(dataInicial) == 0) {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.dataFinal(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant())).build();
					
					List<EstatisticaDiscadorOutputDto> totalizadorBruto = (mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaEnum.getValor(),filter, clienteId)));
					}
				}
				

				List<EstatisticaDiscadorOutputDto> totalizadorBruto = (mapper.modelToCollectionOutputDto(repository.
						findtipoEstatisticaTotalizadorDia(dataAtual, tipoEstatisticaEnum.getValor(),filter, clienteId)));
					
					totalizadorSumarizadoTabela.add(totalizadorBrutoSumarizado);
					
					dataAtual = dataAtual.plusDays(1L);
				}
		
		totalizadorSumarizadoTotal.add(estatisticaProcessada(totalizadorSumarizadoTabela));
		totalizadorSumarizadoTabela.clear();
			
		}
		Long endTime = System.currentTimeMillis();
	//	System.out.printf("\nduração: %f",(float)(endTime-startTime)/1000);
		
		return null;
		//return totalizadorSumarizadoTotal;
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
}
