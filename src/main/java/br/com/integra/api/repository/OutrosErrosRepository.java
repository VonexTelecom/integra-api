package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.OutrosErrosRowMapper;
import br.com.integra.api.model.OutrosErros;
import br.com.integra.api.utils.DateUtils;
import br.com.integra.api.utils.FiltroEstatisticaUtils;

@Repository
public class OutrosErrosRepository {

	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private CountRepository countRepository;
	
	/**
	 * @param date(data da tabela)
	 * @param tipoEstatistica(tipo de estatística(chamadas_ddd) a ser feita na query)
	 * @param filter(Tempoinicial e final passado pelo front)
	 * @param clienteId
	 * @return EstatisticaDiscador
	 */
	
	//Caso o tempo inicial seja customizado(diferente de 00:00:00) e o tempo final não seja informado pelo service esse método recolhe o todos
	// os ddds a partir da data inicial passada pelo front até a 23:59
	public List<OutrosErros> findtipoOutrosErrosInicial(LocalDate date
			, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = DateUtils.formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);
			
		String dataInicialFormatada = DateUtils.formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		String dataFinalFormatada = DateUtils.formatarData(dataFinal);

		String nomeDaTabelaData = String.format("OutrosErros%s", dataFormatada);
				
		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQueryOutrosErros(nomeDaTabelaData, "", filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);
		
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<OutrosErros> listaOutrosErros = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<OutrosErros>
	    	(new OutrosErrosRowMapper()));
	    return listaOutrosErros;
	}
	
	//query feita apartir das 00:00:00 até a data final passada pelo front
	public List<OutrosErros> findtipoOutrosErrosFinal(LocalDate date, EstatisticaFilter filter, Long clienteId) {
		LocalDateTime dataInicial =filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		String dataFormatada = DateUtils.formatarData(date);
		
		//verifição de data do enum (de 8 às 18)
		if(filter.getDataInicial() != null &&
			filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().compareTo(LocalTime.of(8, 00)) == 0){
				dataInicial = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 00);
		}
		
		
		String dataInicialFormatada = DateUtils.formatarData(dataInicial);
		
		String dataFinalFormatada = DateUtils.formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		String nomeDaTabelaData = String.format("OutrosErros%s", dataFormatada);
				
		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQueryOutrosErros(nomeDaTabelaData, "", filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);

		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<OutrosErros> listaOutrosErros = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<OutrosErros>(new OutrosErrosRowMapper()));
		
	    return listaOutrosErros;
	}
	
	//Método para query sem data inicial e data final (busca todos os dados da tabela inteira)
	public List<OutrosErros> findtipoOutrosErros(LocalDate date
			, EstatisticaFilter filter, Long clienteId) {
		
		String dataFormatada = DateUtils.formatarData(date);
		
		//montagem do nome da tabela a ser percorrida na query
		String nomeDaTabelaData = String.format("OutrosErros%s", dataFormatada);

		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQueryOutrosErros(nomeDaTabelaData, "", filter, clienteId, null, null, null, null);
				
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<OutrosErros> listaOutrosErros = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<OutrosErros>
	    (new OutrosErrosRowMapper()));
	    return listaOutrosErros;
	    }
}
