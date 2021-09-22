package br.com.integra.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.dto.output.ValoresCapsOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaCapsMapper;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaCapsRepository;
import br.com.integra.api.utils.DateUtils;

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
	private EstatisticaCapsMapper mapper;
	
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
			
			//lista de caps já processados e devidamente validados para o retorno ao front
			List<EstatisticaCapsOutputDto> capsProcessado = new ArrayList<>();
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
			capsProcessado.add(separadorCaps(capsBruto, dataInicial, dataFinal));
			
			List<ValoresCapsOutputDto> chamadasDiscadas = new ArrayList<>(); 
			List<ValoresCapsOutputDto> maxCapsSainte = new ArrayList<>(); 
			capsProcessado.stream().forEach(e -> chamadasDiscadas.addAll(e.getChamadasDiscadas()));
			capsProcessado.stream().forEach(e -> maxCapsSainte.addAll(e.getMaxCapsSainte()));
			
			EstatisticaCapsOutputDto capsRetorno = EstatisticaCapsOutputDto.builder()
					.chamadasDiscadas(chamadasDiscadas)
					.maxCapsSainte(maxCapsSainte).build();
					

			
			//Condição que certifica que exista algo na tabela, caso não, ele retorna uma lista vazia
			if(!capsProcessado.stream().filter(t -> t.getChamadasDiscadas().stream().filter(v -> v.getQuantidade().longValue() > 0).findAny().isPresent()).findAny().isPresent()) {
				if(!capsProcessado.stream().filter(t -> t.getMaxCapsSainte().stream().filter(v -> v.getQuantidade().longValue() > 0).findAny().isPresent()).findAny().isPresent()) {
					capsRetorno = EstatisticaCapsOutputDto.builder()
							.chamadasDiscadas(new ArrayList<>())
							.maxCapsSainte(new ArrayList<>()).build();
				}
			}
		return capsRetorno;
	}
	/**
	 * @param lista
	 * @param dataInicial
	 * @param dataFinal
	 * @return valores do caps devidamente processados
	 */
	public EstatisticaCapsOutputDto separadorCaps(List<EstatisticaDiscador> lista, LocalDateTime dataInicial, LocalDateTime dataFinal){
		List<ValoresCapsOutputDto> maxCapsSainte = new ArrayList<>();
		List<ValoresCapsOutputDto> chamadasDiscadas = new ArrayList<>();
		List<ValoresCapsOutputDto> listaValores = new ArrayList<>();
		while (dataInicial.compareTo(dataFinal) <= 0) {
			LocalDateTime dataFim = dataInicial.plusMinutes(1L);
			LocalDateTime dataIni = dataInicial;
			List<EstatisticaDiscadorOutputDto> caps = estatisticaMapper.modelToCollectionOutputDto(lista.stream().filter
					(c -> c.getData().compareTo (dataIni) == 0 &&
					c.getData().compareTo(dataFim) <= 0).collect(Collectors.toList()));
			listaValores.addAll(mapper.modelToOutputDto(caps,dataInicial));
			if(listaValores.size() != 0) {
				chamadasDiscadas.add(listaValores.get(1));
				maxCapsSainte.add(listaValores.get(0));
			}
			listaValores.clear();
			dataInicial = dataInicial.plusMinutes(1L);
		}
		EstatisticaCapsOutputDto capsRetorno = EstatisticaCapsOutputDto.builder()
				.chamadasDiscadas(chamadasDiscadas)
				.maxCapsSainte(maxCapsSainte).build();
		
		return capsRetorno;
	}
}
