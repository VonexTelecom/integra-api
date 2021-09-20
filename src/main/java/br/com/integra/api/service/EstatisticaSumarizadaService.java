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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.dto.output.OutrosErrosOutputDto;
import br.com.integra.api.enums.EstatisticaSumarizadaEnum;
import br.com.integra.api.exception.EntidadeNaoEncontradaException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaSumarizada;
import br.com.integra.api.model.OutrosErros;
import br.com.integra.api.repository.EstatisticasSumarizadaRepository;
import br.com.integra.api.repository.OutrosErrosRepository;
import br.com.integra.api.utils.DateUtils;
import br.com.integra.api.utils.SumarizacaoEstatisticaSumarizada;

@Service
public class EstatisticaSumarizadaService {
	
	@Autowired
	private EstatisticasSumarizadaRepository repository;	

	@Autowired
	private OutrosErrosRepository outrosErrosRepository;	
	
	@Autowired
	private SumarizacaoEstatisticaSumarizada sumarizacao;
	
	
	public Page<OutrosErrosOutputDto> findOutrosErros(EstatisticaFilter filter, Long clienteId, Pageable pageable){
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal ;
		//Lista de retono
		
		List<OutrosErrosOutputDto>  listOutrosErrosOutputDto = new ArrayList<>();
		
		//condição para conversão
		if(filter.getPeriodoEnum() != null) {
			dataInicial = DateUtils.converterEnumToData(filter.getPeriodoEnum()).get(0);
			dataFinal = DateUtils.converterEnumToData(filter.getPeriodoEnum()).get(1);			
		}else {
			if(filter.getDataInicial() == null || filter.getDataInicial() == null) {
				throw new EntidadeNaoEncontradaException("Data de Início e Data Final devem ser informadas!") {
					private static final long serialVersionUID = -5461664571083553002L;
				};
			}else {
				dataInicial = filter.getDataInicial().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();
				
				dataFinal = filter.getDataFinal().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();;
			}			
		}
		
		LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		
		List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
		
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		//Verificação da data que vai percorrer a tabela à data final descrita no filtro
		dataIntervalo.stream().parallel().forEachOrdered(dataAtual -> executorService.execute(()-> {
			try {		
				List<OutrosErros> listaOutrosErros = new ArrayList<>();
					
					//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia) caso não ele passa pro repositório apenas a data inicial(data e hora)
					if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();

						listaOutrosErros.addAll(outrosErrosRepository.findtipoOutrosErrosInicial(dataAtual, filtro,clienteId));
					//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
					}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();
						
						listaOutrosErros.addAll(outrosErrosRepository.findtipoOutrosErros(dataAtual, filtro,clienteId));
					//caso a data atual(ano, mês e dia) for igual a data final(ano, mês e dia) o filtro é passado com as datas(data e hora)
					}else {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
								.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();
						
						listaOutrosErros.addAll(outrosErrosRepository.findtipoOutrosErrosFinal(dataAtual, filtro,clienteId));
					
					}
					
					OutrosErrosOutputDto outrosErrosOutputDto;					
					
					for(int i=0; i < listaOutrosErros.size(); i++) {
						outrosErrosOutputDto = new OutrosErrosOutputDto();
						outrosErrosOutputDto.setStatusChamada(listaOutrosErros.get(i).getStatus_chamada());
						outrosErrosOutputDto.setQuantidade(listaOutrosErros.get(i).getQuantidade());
						outrosErrosOutputDto.setDescricao("Descricão do Status : "+listaOutrosErros.get(i).getStatus_chamada());
						listOutrosErrosOutputDto.add(outrosErrosOutputDto);
					}
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
		})); 
		try {
			executorService.shutdown();
			executorService.awaitTermination(30, TimeUnit.MINUTES);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		Page<OutrosErrosOutputDto> page = new PageImpl<>(listOutrosErrosOutputDto.subList((int) pageable.getOffset(),(int) (pageable.getOffset()+pageable.getPageSize())), pageable, listOutrosErrosOutputDto.size());
		return page;
		
	}
	
	
	public EstatisticaSumarizadaOutputDto findPorPeriodo(EstatisticaFilter filter, Long clienteId){
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal ;
		
		//condição para conversão
		if(filter.getPeriodoEnum() != null) {
			dataInicial = DateUtils.converterEnumToData(filter.getPeriodoEnum()).get(0);
			dataFinal = DateUtils.converterEnumToData(filter.getPeriodoEnum()).get(1);			
		}else {
			if(filter.getDataInicial() == null || filter.getDataInicial() == null) {
				throw new EntidadeNaoEncontradaException("Data de Início e Data Final devem ser informadas!") {
					private static final long serialVersionUID = -5461664571083553002L;
				};
			}else {
				dataInicial = filter.getDataInicial().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();
				
				dataFinal = filter.getDataFinal().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime();;
			}			
		}
		
		LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		List<EstatisticaSumarizadaOutputDto> estatisticaSumarizadaTabela = new ArrayList<>();
		List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		//Verificação da data que vai percorrer a tabela à data final descrita no filtro
		dataIntervalo.stream().parallel().forEachOrdered(dataAtual -> executorService.execute(()-> {
			try {		
				List<EstatisticaSumarizada> chamadasSumarizadaBruto = new ArrayList<>();
					
					//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia) caso não ele passa pro repositório apenas a data inicial(data e hora)
					if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();

						chamadasSumarizadaBruto.addAll(repository.findtipoEstatisticaSumarizadaInicial(dataAtual, filtro,clienteId));
						//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
					}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();
						
						chamadasSumarizadaBruto.addAll(repository.findtipoEstatisticaSumarizada(dataAtual, filtro,clienteId));
						//caso a data atual(ano, mês e dia) for igual a data final(ano, mês e dia) o filtro é passado com as datas(data e hora)
					}else {
						EstatisticaFilter filtro = EstatisticaFilter.builder()
								.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
								.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
								.modalidade(filter.getModalidade())
								.discador(filter.getDiscador())
								.operadora(filter.getOperadora())
								.unidadeAtendimento(filter.getUnidadeAtendimento())
								.build();
			
						chamadasSumarizadaBruto.addAll(repository.findtipoEstatisticaSumarizadaFinal(dataAtual, filtro,clienteId));
					
					}
					
					List<EstatisticaSumarizadaEnum> list = Arrays.asList(EstatisticaSumarizadaEnum.values());
					List<EstatisticaSumarizada> separador = new ArrayList<>();
					for(EstatisticaSumarizadaEnum estatistica : list) {
						separador.addAll(chamadasSumarizadaBruto.stream().filter(e -> e.getTipoEstatistica()
								.equals(estatistica.getValor())).collect(Collectors.toList()));
							
					}
					EstatisticaSumarizadaOutputDto estatisticaTotalTabela =	EstatisticaSumarizadaOutputDto.builder()
						.clienteId(clienteId)
						.chamadasDiscadas(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_discadas")).collect(Collectors.toList())))
						.chamadasCompletadas(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_completadas")).collect(Collectors.toList())))
						.chamadasCompletadas3Segundos(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_completadas_ate_3_segundos")).collect(Collectors.toList())))
						.chamadasCompletadas10Segundos(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_completadas_ate_10_segundos")).collect(Collectors.toList())))
						.chamadasCompletadas30Segundos(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_completadas_ate_30_segundos")).collect(Collectors.toList())))
						.chamadasCompletadasMais30Segundos(quantidadeTotal(separador.stream().filter(e -> e.getTipoEstatistica().equals("chamadas_completadas_com_mais_de_30_segundos")).collect(Collectors.toList())))
						.build();
					estatisticaSumarizadaTabela.add(estatisticaTotalTabela);
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
		})); 
		try {
			executorService.shutdown();
			executorService.awaitTermination(30, TimeUnit.MINUTES);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		return somarTabelas(estatisticaSumarizadaTabela, clienteId);
		
	}	
	public EstatisticaSumarizadaOutputDto somarTabelas(List<EstatisticaSumarizadaOutputDto> lista, Long clienteId) {
		EstatisticaSumarizadaOutputDto estatisticaSumarizada = EstatisticaSumarizadaOutputDto.
				builder()
				.clienteId(clienteId)
				.chamadasDiscadas(sumarizacao.chamadasDiscadasTotal(lista))
				.chamadasCompletadas(sumarizacao.chamadasCompletadasTotal(lista))
				.chamadasCompletadas3Segundos(sumarizacao.chamadasCompletadas3SegundosTotal(lista))
				.chamadasCompletadas10Segundos(sumarizacao.chamadasCompletadas10SegundosTotal(lista))
				.chamadasCompletadas30Segundos(sumarizacao.chamadasCompletadas30SegundosTotal(lista))
				.chamadasCompletadasMais30Segundos(sumarizacao.chamadasCompletadasMais30SegundosTotal(lista))
				.build();
		
		return estatisticaSumarizada;
	}
	
	private BigDecimal quantidadeTotal(List<EstatisticaSumarizada> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getQuantidade())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	
	
		
}
