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
import br.com.integra.api.mapper.EstatisticaDiscadorRowMapper;
import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.utils.DateUtils;
import br.com.integra.api.utils.FiltroEstatisticaUtils;

@Repository
public class EstatisticaCapsRepository {
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private CountRepository countRepository;
	
	/**
	 * @param date(data da tabela)
	 * @param tipoEstatistica(tipo de estatística(max_caps_sainte e/ou chamadas_discadas) a ser feita na query)
	 * @param filter(Tempo inicial e final passado pelo front)
	 * @param clienteId
	 * @return EstatisticaDiscador
	 */
	
	//caso o tempo inicial seja customizado(diferente de 00:00:00) e o tempo final não seja informado esse método recolhe todos
	//os dados a partir da data inicial passada pelo front até a 23:59
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorInicial(LocalDate date, EstatisticaFilter filter,Long clienteId){
		
		String dataFormatada = DateUtils.formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);

		String dataInicialFormatada = DateUtils.formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		String dataFinalFormatada = DateUtils.formatarData(dataFinal);

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		String tipoEstatistica = "caps";
		//condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, tipoEstatistica, filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);

		List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
			(new EstatisticaDiscadorRowMapper()));
		
		return estatisticaBruta;
	}
	
	//Método para query sem data inicial e data final (busca todos os dados da tabela inteira)
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizador(LocalDate date, EstatisticaFilter filter, Long clienteId) {
	
		//conversão da data Atual(data da tabela a ser percorrida na query(yyyy-mm-dd)) em string formatada(yyyyMMdd)
		String dataFormatada = DateUtils.formatarData(date);

		//montagem do nome da tabela a ser percorrida na query
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		
		//validação de filtros para query na tabela
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}

		String tipoEstatistica = "caps";

		//condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, tipoEstatistica, filter, clienteId, null, null, null, null);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	
	//Query feita apartir das 00:00:00 até a data final passada pelo front
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorFinal(LocalDate date, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = DateUtils.formatarData(date);
		LocalDateTime dataInicial =filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		
		String tipoEstatistica = "caps";
		
		//verifição de data do enum (de 8 às 18)
				if(filter.getDataInicial() != null &&
					filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().compareTo(LocalTime.of(8, 00)) == 0){
						dataInicial = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 00);
				}
		
		String dataInicialFormatada = DateUtils.formatarData(dataInicial);
		
		String dataFinalFormatada = DateUtils.formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		//validação de filtros para query na tabela
		if(filter.getModalidade().size() == 0 && filter.getDiscador().size() == 0 
				&& filter.getOperadora().size() == 0 && filter.getUnidadeAtendimento().size() == 0) {
			nomeDaTabelaData = String.format("EstatisticaDiscadorDiaSumarizado%s", dataFormatada);
		}
		//condição para a verificação de tabela existente
		//caso não, ela retorna uma lista vazia
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			return new ArrayList<>();
		}
		
		//aplicação dos filtros passados pelo front
		String sql = FiltroEstatisticaUtils.criarQuery(nomeDaTabelaData, tipoEstatistica, filter, clienteId, null, null, dataInicialFormatada, dataFinalFormatada);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }

}
