package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaSumarizadaRowMapper;
import br.com.integra.api.model.EstatisticaSumarizada;
import br.com.integra.api.utils.FiltroEstatisticaUtils;

@Repository
public class EstatisticasSumarizadaRepository {

	
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
	public List<EstatisticaSumarizada> findtipoEstatisticaSumarizadaInicial(LocalDate date
			, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);
			
		String dataInicialFormatada = formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
		
		String dataFinalFormatada = formatarData(dataFinal.toLocalTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if(filter.getModalidade() == null && filter.getDiscador() == null 
				&& filter.getOperadora() == null && StringUtils.isBlank(filter.getUnidadeAtendimento()) == true) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		
		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();

		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, "", filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);
		
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<EstatisticaSumarizada> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaSumarizada>
	    (new EstatisticaSumarizadaRowMapper()));
	    return estatisticaBruta;
	    }
	
	//query feita apartir das 00:00:00 até a data final passada pelo front
	public List<EstatisticaSumarizada> findtipoEstatisticaSumarizadaFinal(LocalDate date
			, EstatisticaFilter filter, Long clienteId) {
		LocalDateTime dataInicial =filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		String dataFormatada = formatarData(date);
		
		//verifição de data do enum (de 8 às 18)
		if(filter.getDataInicial() != null &&
			filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().compareTo(LocalTime.of(8, 00)) == 0){
				dataInicial = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 00);
		}
		
		
		String dataInicialFormatada = formatarData(dataInicial.toLocalTime());
		
		String dataFinalFormatada = formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if(filter.getModalidade() == null && filter.getDiscador() == null 
				&& filter.getOperadora() == null && StringUtils.isBlank(filter.getUnidadeAtendimento()) == true) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		
		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
			
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, "", filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);
		
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<EstatisticaSumarizada> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaSumarizada>
	    (new EstatisticaSumarizadaRowMapper()));
	    return estatisticaBruta;
	    }
	
	//Método para query sem data inicial e data final (busca todos os ddds da tabela inteira)
	public List<EstatisticaSumarizada> findtipoEstatisticaSumarizada(LocalDate date
			, EstatisticaFilter filter, Long clienteId) {
		
		String dataFormatada = formatarData(date);
		
		
		//montagem do nome da tabela a ser percorrida na query
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		//validação de filtros para query na tabela
		if(filter.getModalidade() == null && filter.getDiscador() == null 
				&& filter.getOperadora() == null && StringUtils.isBlank(filter.getUnidadeAtendimento()) == true) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		//Condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, "", filter, clienteId, null, null, null, null);
		
		//conversor da lista dos resultados da query em lista de entidades do spring
	    List<EstatisticaSumarizada> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaSumarizada>
	    (new EstatisticaSumarizadaRowMapper()));
	    return estatisticaBruta;
	    }
	
	
	//método para conversão de LocalDate(yyyy-MM-dd) para string(yyyyMMdd)
	public String formatarData(LocalDate date) {

		return date.format(DateTimeFormatter.BASIC_ISO_DATE).toString();
	}
	//método para conversão de LocalTime para String
	public String formatarData(LocalTime date) {	
		return date.format(DateTimeFormatter.ISO_TIME).toString();
	}
}