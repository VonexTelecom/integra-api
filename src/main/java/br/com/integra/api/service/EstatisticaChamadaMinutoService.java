package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaChamadaMinutoOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaChamadaMinutoRepository;
import br.com.integra.api.utils.DateUtils;

@Service
public class EstatisticaChamadaMinutoService {
	
	@Autowired
	private EstatisticaChamadaMinutoRepository repository;
	
	
	public EstatisticaChamadaMinutoOutputDto discadorTotalizadorMinuto(EstatisticaFilter filter, Long clienteId) {
		

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
			List<EstatisticaDiscador> chamadasBruto = new ArrayList<>();

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
					chamadasBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, filtro,clienteId));
					  
				//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
				}else  if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
					 
					EstatisticaFilter filtro = EstatisticaFilter.builder()
							.modalidade(filter.getModalidade())
							.discador(filter.getDiscador())
							.operadora(filter.getOperadora())
							.unidadeAtendimento(filter.getUnidadeAtendimento())
							.build();
					
					chamadasBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, filtro,clienteId));
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
		
					
					chamadasBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, filtro,clienteId));
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
			EstatisticaChamadaMinutoOutputDto chamadasMinuto = sumarizarChamadasMinuto(chamadasBruto);
			System.out.println("tempo de execução: "+(float)(endTime-startTime)/1000);
		return chamadasMinuto;
	}
	
	public EstatisticaChamadaMinutoOutputDto sumarizarChamadasMinuto (List<EstatisticaDiscador> lista) {
		
		HashMap<LocalDateTime, BigDecimal> chamadasCompletadas = new HashMap<LocalDateTime, BigDecimal>();
		HashMap<LocalDateTime, BigDecimal> chamadasDiscadas = new HashMap<LocalDateTime, BigDecimal>();
		
		List<EstatisticaDiscador> chamadasMinuto = new ArrayList<>();
		List<EstatisticaDiscador> chamadasCompletadasBruto = new ArrayList<>();
		chamadasCompletadasBruto.addAll(lista.stream()
				.filter(e -> e.getTipoEstatistica().equals("chamadas_completadas")).collect(Collectors.toList()));
		
		List<EstatisticaDiscador> chamadasDiscadasBruto = lista.stream()
				.filter(e -> e.getTipoEstatistica().equals("chamadas_discadas")).collect(Collectors.toList());
		
		for (EstatisticaDiscador estatisticaDiscador : chamadasCompletadasBruto) {
			Long tempoValor = Long.valueOf(estatisticaDiscador.getTipoEstisticaValor());
			LocalDateTime dataFinal = estatisticaDiscador.getData().plusMinutes(tempoValor);
			for(LocalDateTime a = estatisticaDiscador.getData(); a.compareTo(dataFinal) <= 0; a = a.plusMinutes(1)) {
				LocalDateTime dataMovel = a;
				if(!chamadasCompletadas.containsKey(dataMovel)) {
					chamadasCompletadas.put(dataMovel, estatisticaDiscador.getQuantidade());
				}else {
						chamadasCompletadas.put(a, chamadasCompletadas.get(dataMovel).add(estatisticaDiscador.getQuantidade()));
				}
				if(!chamadasDiscadasBruto.stream().filter(e->e.getData().compareTo(dataMovel) == 0).findFirst().isPresent()) {
					chamadasDiscadas.put(a, BigDecimal.ZERO);
				}
			}
		}
		for (EstatisticaDiscador estatisticaDiscador : chamadasDiscadasBruto) {
			LocalDateTime dataMovel = estatisticaDiscador.getData();
			if(!chamadasDiscadas.containsKey(dataMovel)) {
					chamadasDiscadas.put(dataMovel, estatisticaDiscador.getQuantidade());
			} else {
					chamadasDiscadas.put(dataMovel, chamadasDiscadas.get(dataMovel).add(estatisticaDiscador.getQuantidade()));
			}
			if(!chamadasCompletadasBruto.stream().filter(e->e.getData().plusMinutes(Long.valueOf(e.getTipoEstisticaValor())).compareTo(dataMovel) >= 0).findFirst().isPresent()) {
					chamadasCompletadas.put(dataMovel, BigDecimal.ZERO);
			}
		}
		
		List<EstatisticaDiscador> chamadasDiscadasRetorno = chamadasDiscadas.entrySet().stream().map(
				e -> new EstatisticaDiscador().builder().data(e.getKey()).quantidade(e.getValue()).tipoEstatistica("chamadas_discadas").build())
				.collect(Collectors.toList());
		
		List<EstatisticaDiscador> chamadasCompletadasRetorno = chamadasCompletadas.entrySet().stream().map(
				e -> new EstatisticaDiscador().builder().data(e.getKey()).quantidade(e.getValue()).tipoEstatistica("chamadas_completadas").build())
				.collect(Collectors.toList());
			
		chamadasDiscadasRetorno.sort((d1, d2) -> d1.getData().compareTo(d2.getData()));
		
		chamadasCompletadasRetorno.sort((d1, d2) -> d1.getData().compareTo(d2.getData()));
		
		System.out.println("\n\n chamadasCompletadas"+chamadasCompletadas+"\n\n chamadasDiscadas:"+chamadasDiscadas);
		
		return EstatisticaChamadaMinutoOutputDto.builder()
				.chamadasDiscadas(chamadasDiscadasRetorno)
				.chamadasCompletadas(chamadasCompletadasRetorno).build();
		
	}
}
