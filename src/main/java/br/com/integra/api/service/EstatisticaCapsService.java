package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaCapsRepository;
import br.com.integra.api.utils.DateUtils;
import javassist.expr.NewArray;

/**
 * @author Rafael Lopes
 * 
 * Service para o processamento do caps
 *
 */
@Service
public class EstatisticaCapsService {
	
	@Autowired
	private EstatisticaCapsRepository repository;
	
	
	@Autowired
	private EstatisticaDiscadorMapper estatisticaMapper;
	
	/**
	 * @param filter
	 * @param clienteId
	 * @return EstatisticaCapsOutputDto
	 * 
	 * O método recebe o filtro contendo uma data inicial, data final(pode ser por enum ou não) e o id do cliente
	 */
	public EstatisticaCapsOutputDto discadorTotalizadorCaps(EstatisticaFilter filter, Long clienteId) {
		
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
	
		//condição para conversão do enum em data caso seja marcado e validação da data inicial e data final
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
			
			LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
			LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
			
			List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
			ExecutorService executorService = Executors.newFixedThreadPool(8);
			List<EstatisticaDiscador> capsBruto = new ArrayList<>();

			//verificação da data que vai percorrer a tabela até a data final descrita no filtro
			dataIntervalo.stream().parallel().forEachOrdered(dataAtual -> executorService.execute(() ->{
				try {
				//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia), caso sim ele passa pro repositório apenas a data inicial(data e hora)
				if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					capsBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, filtro,clienteId));
					  
				//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
				}else  if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					 
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					
					capsBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, filtro,clienteId));
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
		
					
					capsBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, filtro,clienteId));
				}
				}catch(Exception e){
					System.out.println(e.getStackTrace());
				}		
			}));
			try {
				executorService.shutdown();
				executorService.awaitTermination(3000, TimeUnit.MINUTES);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
			
			Long endTime = System.currentTimeMillis();
			EstatisticaCapsOutputDto caps = sumarizarCaps(capsBruto);
			System.out.println("tempo de execução: "+(float)(endTime-startTime)/1000);
		return caps;
	}
	
	public EstatisticaCapsOutputDto sumarizarCaps (List<EstatisticaDiscador> lista) {
		Set<LocalDateTime> datas = lista.stream().map(e -> e.getData()).collect(Collectors.toSet());
		List<LocalDateTime> datasOrdenadas = Lists.newArrayList(datas).stream().sorted().collect(Collectors.toList());
		List<EstatisticaDiscador> caps = new ArrayList<>();
		
		for (LocalDateTime dataAtual : datasOrdenadas) {
			EstatisticaDiscador chamadasDiscadas = EstatisticaDiscador.builder()
					.data(dataAtual)
					.tipoEstatistica("chamadas_discadas")
					.quantidade(lista.stream()
							.filter(e -> e.getData().compareTo(dataAtual) == 0 && e.getTipoEstatistica()
							.equals("chamadas_discadas")).map(q -> q.getQuantidade()).reduce(BigDecimal.ZERO, BigDecimal :: add))
					.build();
			
			EstatisticaDiscador maxCapsSainte = EstatisticaDiscador.builder()
					.data(dataAtual)
					.tipoEstatistica("max_caps_sainte")
					.quantidade(lista.stream()
							.filter(e -> e.getData().compareTo(dataAtual) == 0 && e.getTipoEstatistica()
							.equals("max_caps_sainte")).map(q -> q.getQuantidade()).reduce(BigDecimal.ZERO, BigDecimal::add))
					.build();
			
		caps.add(chamadasDiscadas);
		caps.add(maxCapsSainte);
		}
		
		return EstatisticaCapsOutputDto.builder()
				.chamadasDiscadas(caps)
				.maxCpasSainte(caps).build();
	}
	
	
}
