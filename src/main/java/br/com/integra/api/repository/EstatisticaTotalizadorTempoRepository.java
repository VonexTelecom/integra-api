package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.exception.EntidadeNaoEncontradaException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.mapper.EstatisticaDiscadorRowMapper;
import br.com.integra.api.model.EstatisticaDiscador;

@Repository
public class EstatisticaTotalizadorTempoRepository {
	@Autowired
	private CountRepository countRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorInicial(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId,
			Integer valorInicial, Integer valorFinal) {
		
		String dataFormatada = formatarData(date);
		LocalDateTime dataFinal = filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataFinal = dataFinal.toLocalDate().atTime(23, 59);
		
		String dataInicialFormatada = formatarData(filter.getDataInicial().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		String dataFinalFormatada = formatarData(dataFinal);
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);

		if(countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não Encontrada") {};
		}

		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s' and clienteId = %d and data between '%s' and '%s'",
				nomeDaTabelaData, tipoEstatistica, filter.getModalidade(), clienteId, dataInicialFormatada, dataFinalFormatada);

		if((tipoEstatistica.equals("chamada_com_segundo_desc_origem") || tipoEstatistica.equals("chamada_com_segundo_desc_destino")) 
				&& valorInicial!=null && valorFinal!=null) {
			sql = sql + String.format(" and CAST(tipoEstatiscaValor AS unsigned integer) BETWEEN %d and %d",valorInicial, valorFinal);
		}

		List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql,
				new RowMapperResultSetExtractor<EstatisticaDiscador>(new EstatisticaDiscadorRowMapper()));
		return estatisticaBruta;

	}
	
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorFinal(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId,
			Integer valorInicial, Integer valorFinal) {
		
		String dataFormatada = formatarData(date);
		LocalDateTime dataInicial = filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		dataInicial = dataInicial.toLocalDate().atStartOfDay();
		
		String dataInicialFormatada = formatarData(dataInicial);
		String dataFinalFormatada = formatarData(filter.getDataFinal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);

		if(countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não Encontrada") {};
		}

		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s' and clienteId = %d and data between '%s' and '%s'",
				nomeDaTabelaData, tipoEstatistica, filter.getModalidade(), clienteId, dataInicialFormatada, dataFinalFormatada);

		if((tipoEstatistica.equals("chamada_com_segundo_desc_origem") || tipoEstatistica.equals("chamada_com_segundo_desc_destino")) 
				&& valorInicial!=null && valorFinal!=null) {
			sql = sql + String.format(" and CAST(tipoEstatiscaValor AS unsigned integer) BETWEEN %d and %d",valorInicial, valorFinal);
		}

		List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql,
				new RowMapperResultSetExtractor<EstatisticaDiscador>(new EstatisticaDiscadorRowMapper()));
		return estatisticaBruta;

	}
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizador(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId,
			Integer valorInicial, Integer valorFinal) {
		
		String dataFormatada = formatarData(date);
		
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);

		if(countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não Encontrada") {};
		}

		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s' and clienteId = %d",
				nomeDaTabelaData, tipoEstatistica, filter.getModalidade(), clienteId);

		if((tipoEstatistica.equals("chamada_com_segundo_desc_origem") || tipoEstatistica.equals("chamada_com_segundo_desc_destino")) 
				&& valorInicial!=null && valorFinal!=null) {
			sql = sql + String.format(" and CAST(tipoEstatiscaValor AS unsigned integer) BETWEEN %d and %d",valorInicial, valorFinal);
		}

		List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql,
				new RowMapperResultSetExtractor<EstatisticaDiscador>(new EstatisticaDiscadorRowMapper()));
		return estatisticaBruta;

	}
	
	
	
	public String formatarData(LocalDate date) {
		String mes = "" + date.getMonthValue();
		if (date.getMonthValue() <= 9) {
			mes = 0 + "" + date.getMonthValue();
		}
		String dataFormatada = date.getYear() + "" + mes + "" + date.getDayOfMonth();
		return dataFormatada;
	}
	
	public String formatarData(LocalDateTime date) {	
		return date.format(DateTimeFormatter.ISO_DATE_TIME).toString();
	}
	
}
