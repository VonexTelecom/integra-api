package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.exception.EntidadeNaoEncontradaException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorRowMapper;
import br.com.integra.api.model.EstatisticaDiscador;

@Repository
public class EstatisticaCapsRepository {
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private CountRepository countRepository;
	
	@Transactional
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorInicial(LocalDate date, EstatisticaFilter filter,Long clienteId){
		
		String dataFormatada = formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);

		String dataInicialFormatada = formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		String dataFinalFormatada = formatarData(dataFinal);

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {
			};
		}
		
		System.out.print("dataInicial: "+ dataInicialFormatada+ "\n dataFinal"+ dataFinalFormatada);

		String sql = String.format("SELECT * FROM %s where modalidade = '%s' and clienteId = %d"
				+ " and data between '%s' and '%s' and tipoEstatistica = 'max_caps_sainte' or "
				+ "tipoEstatistica = 'chamadas_discadas'",
				nomeDaTabelaData, filter.getModalidade(), clienteId,  dataInicialFormatada, dataFinalFormatada);

		List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
			(new EstatisticaDiscadorRowMapper()));
		
		return estatisticaBruta;
	}
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizador(LocalDate date, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);
		
		

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		System.out.println(nomeDaTabelaData);
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {
			};
		}
		
		String sql = String.format("SELECT * FROM %s where modalidade = '%s' and clienteId = %d"
				+ " and tipoEstatistica = 'max_caps_sainte' or "
				+ "tipoEstatistica = 'chamadas_discadas'",
	    		nomeDaTabelaData, filter.getModalidade(), clienteId);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorFinal(LocalDate date, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);
		LocalDateTime dataInicial =filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		
		String dataInicialFormatada = formatarData(dataInicial);
		
		String dataFinalFormatada = formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		System.out.println(nomeDaTabelaData);
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {
			};
		}
		
		String sql = String.format("SELECT * FROM %s where modalidade = '%s' and clienteId = %d"
				+ " and data between '%s' and '%s' and tipoEstatistica = 'max_caps_sainte' or "
				+ "tipoEstatistica = 'chamadas_discadas'",
	    		nomeDaTabelaData, filter.getModalidade(), clienteId,dataInicialFormatada, dataFinalFormatada);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;
	    }
	

	public String formatarData(LocalDate date) {
		String mes =""+ date.getMonthValue();
		String dia = "" + date.getDayOfMonth();
		if (date.getMonthValue() <= 9) {
			mes = "0" + date.getMonthValue();
		}
		if(date.getDayOfMonth() <= 9) {
			dia = "0" + date.getDayOfMonth();
		}
		String dataFormatada = date.getYear() + "" + mes + "" + dia;

		return dataFormatada;
	}
	
	public String formatarData(LocalDateTime date) {	
		return date.format(DateTimeFormatter.ISO_DATE_TIME).toString();
	}

}
