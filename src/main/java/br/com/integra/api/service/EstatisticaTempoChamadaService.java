package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaTotalizadorTempoRepository;
import br.com.integra.api.utils.DateUtils;

@Service
public class EstatisticaTempoChamadaService {
	
	@Autowired
	private EstatisticaTotalizadorTempoRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	/**
	 * @param filter
	 * @param clienteId
	 * @return EstatististicaDistcadorOutputDto
	 * 
	 * O método recebe  o filtro contendo uma data inicial, data final(pode ser por enum ou não) e o id do cliente
	 */
	public List<EstatisticaDiscadorOutputDto> discadorTotalizadorTempoChamadas(EstatisticaFilter filter, Long clienteId){

		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
	
		//Condição para conversão do enum em data caso seja marcado, e validação da data inicial e data final
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
		
		//lista de resultados direto da tabela ja válidados
		List<EstatisticaDiscadorOutputDto> chamadaBrutoTabela = new ArrayList<>();
		
		//lista de resultados de todas as tabelas ja processados
		List<EstatisticaDiscadorOutputDto> chamadaProcessada = new ArrayList<>();
		
		//lista de resultados da tabela processados
		List<EstatisticaDiscadorOutputDto> chamadaTabelaProcessada = new ArrayList<>();
			
				
			
		LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		
		
		dataIntervalo.stream().parallel().forEachOrdered(dataAtual -> executorService.execute(() -> {
			try {
			
			//lista de resultados direto da tabela não validados
			List<EstatisticaDiscador> chamadasOrigemBruto = new ArrayList<>();
			//lista de resultados direto da tabela não validados
			List<EstatisticaDiscador> chamadasDestinoBruto = new ArrayList<>();
			String tipoEstatisticaOrigem = String.format("chamada_com_segundo_desc_origem");
			String tipoEstatisticaDestino = String.format("chamada_com_segundo_desc_destino");
			String tipoEstatisticaTotal = String.format("chamada_com_segundo_desc_total");
			
		
			//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia), caso sim ele passa pro repositório apenas a data inicial(data e hora)
			if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.build();
				
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));

			//caso a data atual for diferente da data inicial(ano, mês e dia) e menor que a data final(ano, mês e dia) o filtro é passado sem as datas
			}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
				
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.build();
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));
				
			//caso a data atual(ano, mês e dia) for igual a data final(ano, mês e dia) o filtro é passado com as datas(data e hora)
			}else {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.dataFinal(Date.from(dataFinal.atZone(ZoneId.systemDefault()).toInstant()))
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.build();
				chamadasOrigemBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaOrigem, filtro, clienteId,0,120));
					
				
				chamadasDestinoBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatisticaDestino, filtro, clienteId,0,120));
			}

			//loop para validação de resultados inexistentes na tabela
			for (int i = 0; i<=120; i++) {
				int a = i;
					//a instancia verifica se o valor esxiste, caso não, ela o atribui com o valor de quantidade em zero
					EstatisticaDiscador estatisticaOrigem =  chamadasOrigemBruto.stream().filter(chamada ->
						chamada.getTipoEstisticaValor().equals(String.valueOf(a)) && chamada.getTipoEstatistica().equals(tipoEstatisticaOrigem) )
						.findFirst().orElseGet(() -> Optional.of(
								EstatisticaDiscador.builder()
									.tipoEstatistica(tipoEstatisticaOrigem)
									.quantidade(BigDecimal.ZERO)
									.tipoEstisticaValor(String.valueOf(a))
									.build()).get());
						
					//a instancia verifica se o valor existe, caso não, ela o atribui com o valor de quantidade em zero
					EstatisticaDiscador estatisticaDestino =  chamadasDestinoBruto.stream().filter(chamada ->
					chamada.getTipoEstisticaValor().equals(String.valueOf(a))  &&  chamada.getTipoEstatistica().equals(tipoEstatisticaDestino))
					.findFirst().orElseGet(() -> Optional.of(
							EstatisticaDiscador.builder()
								.tipoEstatistica(tipoEstatisticaDestino)
								.quantidade(BigDecimal.ZERO)
								.tipoEstisticaValor(String.valueOf(a))
								.build()).get());
				 
					
					//Instancia que armazena a soma dos dois tipos de estatistica
					EstatisticaDiscador estatisticaTotal = EstatisticaDiscador.builder()
							.quantidade(estatisticaDestino.getQuantidade().add(estatisticaOrigem.getQuantidade()))
							.tipoEstatistica(tipoEstatisticaTotal)
							.tipoEstisticaValor(String.valueOf(a)).build();
					
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaOrigem));
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaDestino));
					chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundo(estatisticaTotal));
					 
			
			}
			//processamento de cada item da tabela
			chamadaTabelaProcessada.addAll(somaTabela(chamadaBrutoTabela));
			
			chamadaBrutoTabela.clear();
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
		
		chamadaProcessada.addAll(somaTabela(chamadaTabelaProcessada));
		//Condição que certifica que exista algo na tabela, caso não, ele retorna uma lista vazia
		if(!chamadaProcessada.stream().anyMatch(c -> c.getQuantidade().longValue() > 0)) {
			return new ArrayList<>();
		}
		return chamadaProcessada;
		
	}
	//Método que soma a quantidade de cada chamada por tabela 
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
	//método que soma a quantidade de cada ddds por tabela
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	
}
