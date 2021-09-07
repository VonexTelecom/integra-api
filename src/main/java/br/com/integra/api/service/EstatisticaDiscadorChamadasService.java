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
import br.com.integra.api.enums.TipoEstatisticaEnum;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.repository.EstatisticaTotalizadorChamadasRepository;
import br.com.integra.api.utils.DateUtils;

@Service
public class EstatisticaDiscadorChamadasService {
	
	@Autowired
	private EstatisticaTotalizadorChamadasRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	/**
	 * @param filter
	 * @param clienteId
	 * @return EstatisticaDiscadorOutputDto
	 */
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorChamadas(EstatisticaFilter filter, Long clienteId) {
		
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
		
		//Condição para validação  e conversão do enum em data caso seja marcado, e validação da data inicial e data final
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
		
		//lista de unums a serem percorridos por tabela
		List<TipoEstatisticaEnum> list = Arrays.asList(TipoEstatisticaEnum.values());
		
		//Total dos resultados da tabela devidamente sumarizados
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTabela = new ArrayList<>();

		//Total dos resultados de todas as tabelas
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTotal = new ArrayList<>();
		
		//loop de tipos da estatistica a ser percorrido na query e processados por tabela
		for (TipoEstatisticaEnum tipoEstatisticaEnum : list) {
			EstatisticaDiscadorOutputDto totalizadorBrutoSumarizado = new EstatisticaDiscadorOutputDto();
			LocalDate dataAtual = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			
			//lista de estatistica vindas direto do banco
			List<EstatisticaDiscadorOutputDto> totalizadorBruto = new ArrayList<>();

			//Verificação da data que vai percorrer a tabela à data final descrita no filtro
			while(dataAtual.compareTo(dataFinalFormatada) <= 0){
				
				//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia) caso não ele passa pro repositório apenas a data inicial(data e hora)
				if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
					
				//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
				}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
				
				//caso a data atual(ano, mês e dia) for igual a data final(ano, mês e dia) o filtro é passado com as datas(data e hora)
				}else{
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaEnum.getValor(),filtro, clienteId)));
					
					
				}
				//instancia para a sumarização de um tipo de estistica por tabela
				totalizadorBrutoSumarizado = EstatisticaDiscadorOutputDto.builder()
							.tipoEstatistica(tipoEstatisticaEnum.getValor())
							.quantidade(quantidadeTotal(totalizadorBruto)).build();
					
					totalizadorSumarizadoTabela.add(totalizadorBrutoSumarizado);
					
					dataAtual = dataAtual.plusDays(1L);
				}
			
		totalizadorBruto.clear();
		//sumarização dos resultados das tabelas
		totalizadorSumarizadoTotal.add(estatisticaProcessada(totalizadorSumarizadoTabela));
		totalizadorSumarizadoTabela.clear();
			
		}

		Long endTime = System.currentTimeMillis();
		System.out.printf("\nduração: %f",(float)(endTime-startTime)/1000);
		
		return totalizadorSumarizadoTotal;
	}
	
	//método para a sumarização da quantidade de uma lista de estatistica já sumarizadas
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
	
	//método que soma a quantidade de cada tipo de estatistica por tabela
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	//verificação do intervalo da data
	public List<LocalDate> verificaIntervaloData(
			LocalDate startDate, LocalDate endDate) { 
		
		long dias = ChronoUnit.DAYS.between(startDate, endDate); 
		return IntStream.iterate(0, i -> i + 1)
				.limit(dias)
				.mapToObj(i -> startDate.plusDays(i))
				.collect(Collectors.toList()); 
	}
}
