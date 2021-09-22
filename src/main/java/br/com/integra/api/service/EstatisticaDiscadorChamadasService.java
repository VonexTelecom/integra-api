package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		
		//Total dos resultados da tabela devidamente sumarizados
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTabela = new ArrayList<>();

		//Total dos resultados de todas as tabelas
		List<EstatisticaDiscadorOutputDto> totalizadorSumarizadoTotal = new ArrayList<>();
		
		//loop de tipos da estatistica a ser percorrido na query e processados por tabela
			LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);

			//lista de estatistica vindas direto do banco
			List<EstatisticaDiscadorOutputDto> totalizadorBruto = new ArrayList<>();

			//Verificação da data que vai percorrer a tabela à data final descrita no filtro
			for (LocalDate dataAtual : dataIntervalo) {
				///condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia) caso não ele passa pro repositório apenas a data inicial(data e hora)
				if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorInicial(dataAtual, filtro, clienteId)));
					
				//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
				}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizador(dataAtual, filtro, clienteId)));
				
				//caso a data atual(ano, mês e dia) for igual a data final(ano, mês e dia) o filtro é passado com as datas(data e hora)
				}else{
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					
					totalizadorBruto.addAll(mapper.modelToCollectionOutputDto(repository.
							findtipoEstatisticaTotalizadorFinal(dataAtual, filtro, clienteId)));
					
				}
			
				//instancia para a sumarização de um tipo de estistica por tabela
				totalizadorSumarizadoTabela.addAll(estatisticaProcessada(totalizadorBruto));
				
				totalizadorBruto.clear();
		}
			
		//sumarização dos resultados das tabelas
		totalizadorSumarizadoTotal.addAll(estatisticaProcessada(totalizadorSumarizadoTabela));
		Long endTime = System.currentTimeMillis();
		System.out.printf("\nduração: %f",(float)(endTime-startTime)/1000);
		
		return totalizadorSumarizadoTotal;
		}


	//método para a sumarização da quantidade de uma lista de estatistica já sumarizadas
	public List<EstatisticaDiscadorOutputDto> estatisticaProcessada(List<EstatisticaDiscadorOutputDto> listaBruta){
		List<TipoEstatisticaEnum> listaEnum = Arrays.asList(TipoEstatisticaEnum.values());
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		List<EstatisticaDiscadorOutputDto> listaSumarizada = new ArrayList<>(); 
			listaEnum.stream().parallel().forEachOrdered(tipoEstatisticaEnum -> executorService.execute(() ->{
				try {
					EstatisticaDiscadorOutputDto totalizadorBrutoSumarizado = EstatisticaDiscadorOutputDto.builder()
							.tipoEstatistica(tipoEstatisticaEnum.getValor())
							.quantidade(quantidadeTotal(listaBruta.stream().filter(e ->
							e.getTipoEstatistica().equals(tipoEstatisticaEnum.getValor())).collect(Collectors.toList()))).build();
					listaSumarizada.add(totalizadorBrutoSumarizado);
					
				}catch(Exception e){
					System.out.println(e.getStackTrace());
				}
			}));
			
			try{
				executorService.shutdown();
				executorService.awaitTermination(30, TimeUnit.MINUTES);
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
			
		return listaSumarizada;
	}
	
	//método que soma a quantidade de cada tipo de estatistica por tabela
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
