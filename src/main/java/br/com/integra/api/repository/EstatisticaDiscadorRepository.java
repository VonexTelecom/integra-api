package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class EstatisticaDiscadorRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private CountRepository countRepository;
	
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorDia(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {
			};
		}
		
		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s' and clienteId = %d",
	    		nomeDaTabelaData, tipoEstatistica, filter.getModalidade(), clienteId);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;

	    }
	
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorDiaAtual(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId) {

		String dataFormatada = formatarData(date);
		
		String dataInicialFormatada = formatarData(filter.getDataFinal());

		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if (countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {
			};
		}
		
		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s' and clienteId = %d and between ",
	    		nomeDaTabelaData, tipoEstatistica, filter.getModalidade(), clienteId);
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new EstatisticaDiscadorRowMapper()));
	    return estatisticaBruta;

	    }
	
	
	
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorDia(LocalDate date, String tipoEstatistica, EstatisticaFilter filter, Long clienteId,
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
		if((tipoEstatistica.equals("chamadas_ddd")) 
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
		return date.toLocalDate().format(DateTimeFormatter.ISO_DATE).toString();

	}


}
