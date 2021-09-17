package br.com.integra.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaDddOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.exception.BusinessException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.repository.EstatisticaTotalizadorDddRepository;
import br.com.integra.api.utils.CoordenadasUtils;
import br.com.integra.api.utils.DateUtils;

/**
 * @author Rafael Lopes
 *	service para o processamento da quantidade de chamadas por ddd
 */
@Service
public class EstatisticaDiscadorDddService {
	
	@Autowired
	private EstatisticaTotalizadorDddRepository repository;
	
	@Autowired
	private EstatisticaDiscadorMapper mapper;
	
	@Autowired
	private CoordenadasUtils coordenadaService;
	
	
	
	/**
	 * @param filter
	 * @param clienteId
	 * @return EstatististicaDddOutputDto
	 * 
	 * O método recebe  o filtro contendo uma data inicial, data final(pode ser por enum ou não) e o id do cliente
	 */
	public List<EstatisticaDddOutputDto>discadorTotalizadorDdd(EstatisticaFilter filter, Long clienteId){
		Long startTime = System.currentTimeMillis();
		
		LocalDateTime dataInicial;
		LocalDateTime dataFinal;
	
		
		
		//condição conversão do enum em data caso seja marcado, e validação da data inicial e data final
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
		
		//lista que será preenchida com os valores brutos de cada tabela já validados
		List<EstatisticaDiscadorOutputDto> chamadaBrutoTabela = new ArrayList<>();
		
		//lista com os valores já sumarizados e prontos para o retorno
		List<EstatisticaDddOutputDto> chamadaProcessada = new ArrayList<>();
		
		
		LocalDate dataAtualPeriodo = LocalDate.of(dataInicial.getYear(), dataInicial.getMonthValue(), dataInicial.getDayOfMonth());
		LocalDate dataFinalFormatada = LocalDate.of(dataFinal.getYear(), dataFinal.getMonthValue(), dataFinal.getDayOfMonth());
		List<LocalDate> dataIntervalo = DateUtils.IntervaloData(dataAtualPeriodo, dataFinalFormatada);
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		
		//Verificação da data que vai percorrer a tabela à data final descrita no filtro
		dataIntervalo.stream().parallel().forEachOrdered(dataAtual -> executorService.execute(() -> {
			try {
			
			String tipoEstatistica = String.format("chamadas_ddd");
			List<EstatisticaDiscador> chamadasDddBruto = new ArrayList<>();
			
			//condição que verifica se a dataAtual(ano, mês e dia) é igual a data inicial(ano, mês e dia) caso não ele passa pro repositório apenas a data inicial(data e hora)
			if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) == 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.dataInicial(Date.from(dataInicial.atZone(ZoneId.systemDefault()).toInstant()))
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.build();

				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizadorInicial(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
				//caso a data atual for diferente da data inicial(ano, mês e dia) e data final(ano, mês e dia) o filtro é passado sem as datas
			}else if(dataAtual.compareTo(dataFinalFormatada) < 0 && dataAtual.compareTo(dataInicial.atZone(ZoneId.systemDefault()).toLocalDate()) != 0) {
				EstatisticaFilter filtro = EstatisticaFilter.builder()
						.modalidade(filter.getModalidade())
						.discador(filter.getDiscador())
						.operadora(filter.getOperadora())
						.unidadeAtendimento(filter.getUnidadeAtendimento())
						.build();
				
				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizador(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
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
	
				chamadasDddBruto.addAll(repository.findtipoEstatisticaTotalizadorFinal(dataAtual, tipoEstatistica, filtro,clienteId, 11,99));
			
			}
			//Loop que vai percorrer cada ddd da tabela e validá-lo como existente e não existente
			for (int i = 11; i<=99; i++) {
					int a = i;
					if(dddInexistente(i) == true) {
						continue;
					}	
					
			//Instancia que verifica o numero do ddd e certifica que o mesmo está presente na tabela
			//caso não, ele o atribui com o valor da quantidade em zero
			List<EstatisticaDiscador> estatisticaTabela =  chamadasDddBruto.stream().filter(chamada ->
				chamada.getTipoEstisticaValor().equals(String.valueOf(a)) && chamada.getTipoEstatistica().equals(tipoEstatistica))
					.collect(Collectors.toList());
			
			EstatisticaDiscador estatistica = EstatisticaDiscador.builder()
					.quantidade(BigDecimal.ZERO)
					.tipoEstisticaValor(String.valueOf(a))
					.tipoEstatistica(tipoEstatistica)
					.build();
			
			if(estatisticaTabela.size() > 0) {
				List<EstatisticaDiscadorOutputDto> estatisticaTabelaSomar = estatisticaTabela.stream().map(e -> mapper.modelToOutputDto(e)).collect(Collectors.toList());
				
				estatistica = EstatisticaDiscador.builder()
						.quantidade(quantidadeTotal(estatisticaTabelaSomar))
						.tipoEstisticaValor(String.valueOf(a))
						.tipoEstatistica(tipoEstatistica)
						.build();
				}
			
			chamadaBrutoTabela.add(mapper.modelToOutputDtoSegundoDDD(estatistica));
				}
			}catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
		}));
		try {
			executorService.shutdown();
			executorService.awaitTermination(3000, TimeUnit.MINUTES);
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		chamadaProcessada.addAll(somaTabela(chamadaBrutoTabela));
		
		//Condição que certifica que exista algo na tabela, caso não, ele retorna uma lista vazia
		if(!chamadaProcessada.stream().anyMatch(c -> c.getQuantidade().longValue() > 0)) {
			return new ArrayList<>();
		}
		Long endTime = System.currentTimeMillis();
		System.out.printf("\ntempo de execução final %f", (float)(endTime - startTime)/1000);
		return chamadaProcessada;
	}
	
	//Método que soma a quantidade de cada ddd por tabela e converte o ddd em coordenada 
	public List<EstatisticaDddOutputDto> somaTabela(List<EstatisticaDiscadorOutputDto> lista){
		List<EstatisticaDddOutputDto> estatisticasProcessadas = new ArrayList<>();
		HashMap<Integer, EstatisticaDddOutputDto> coordenadas = new HashMap<Integer, EstatisticaDddOutputDto>();
		coordenadaService.getCoordenada(coordenadas);
		for (int i = 11; i<=99; i++) {
			if(dddInexistente(i) == true) {
				continue;
			}
			String nome = String.format("chamadas_ddd_%d", i);
			if(lista != null ) {
				
				List<EstatisticaDiscadorOutputDto> estatisticaDdd =  lista.stream().filter(chamada ->
				chamada.getTipoEstatistica().equals(nome)).collect(Collectors.toList());
				EstatisticaDddOutputDto estatistica = EstatisticaDddOutputDto.builder()
						.ddd(i)
						.local(coordenadas.get(i).getLocal())
						.Latitude(coordenadas.get(i).getLatitude())
						.Longitude(coordenadas.get(i).getLongitude())
						.quantidade(quantidadeTotal(estatisticaDdd))
						.build();
				estatisticasProcessadas.add(estatistica);
			}else {
				System.out.println("\n\n\n\n Lista Vazia");
			}
			
		
		}
		return estatisticasProcessadas;
		
	}
	
	//método para reservar todos os ddds inválidos e verifica-los quando chamados pelo loop
	public boolean dddInexistente(int ddd) {
		List<Integer> dddInvalidos = Arrays.asList(20, 23, 25, 26, 29, 30, 36, 39
				, 40, 50, 52, 56, 57, 58, 59, 60, 70, 72, 76, 78, 80, 90);
		boolean dddInvalido = dddInvalidos.stream().anyMatch(valor -> valor == ddd);
		return dddInvalido;
		
	}
	//método que soma a quantidade de cada ddds por tabela
	private BigDecimal quantidadeTotal(List<EstatisticaDiscadorOutputDto> estatisticas) {
		return estatisticas
				.stream()
				.map(estatistica -> estatistica.getQuantidade())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
}
