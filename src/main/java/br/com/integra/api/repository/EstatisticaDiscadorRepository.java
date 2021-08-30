package br.com.integra.api.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.estatisticaDiscadorRowMapper;
import br.com.integra.api.exception.EntidadeNaoEncontradaException;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaDiscador;
@Repository
public class EstatisticaDiscadorRepository  {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	private CountRepository countRepository;
	
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorDia(LocalDate date, String tipoEstatistica, EstatisticaFilter filter) {
		String dataFormatada = formatarData(date);
		
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if(countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {};
		}
	      
		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s'",
	    		nomeDaTabelaData, tipoEstatistica, filter.getModalidade());
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new estatisticaDiscadorRowMapper()));
	    return estatisticaBruta;

	    }
	public List<EstatisticaDiscador> findtipoEstatisticaTotalizadorDia(LocalDate date, String tipoEstatistica, EstatisticaFilter filter,
			Integer segundoInicial, Integer segundoFinal) {
		String dataFormatada = formatarData(date);
		
		String nomeDaTabelaData = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		if(countRepository.VerificaTabelaExistente(nomeDaTabelaData) == false) {
			throw new EntidadeNaoEncontradaException("Data não registrada") {};
		}
	      
		String sql = String.format("SELECT * FROM %s where tipoEstatistica = '%s' and modalidade = '%s'",
	    		nomeDaTabelaData, tipoEstatistica, filter.getModalidade());
		
		if((tipoEstatistica.equals("chamada_com_segundo_desc_origem") || tipoEstatistica.equals("chamada_com_segundo_desc_destino")) 
				&& segundoInicial!=null && segundoFinal!=null) {
			sql = sql + String.format(" and CAST(tipoEstatiscaValor AS unsigned integer) BETWEEN %d and %d",segundoInicial, segundoFinal);
		}
		
	    List<EstatisticaDiscador> estatisticaBruta = namedJdbcTemplate.query(sql, new RowMapperResultSetExtractor<EstatisticaDiscador>
	    (new estatisticaDiscadorRowMapper()));
	    return estatisticaBruta;

	    }
	

	public String formatarData(LocalDate date) {
		String mes = ""+date.getMonthValue();
		if(date.getMonthValue() <= 9) {
			mes = 0+""+date.getMonthValue();
		}
		String dataFormatada = date.getYear()+""+mes+""+date.getDayOfMonth();
		return dataFormatada;
	}
	
	public String formatarDataTime(LocalDateTime date) {
		String mes = String.valueOf(date.getMonthValue());
		String minuto = String.valueOf(date.getMinute());
		String hora = String.valueOf(date.getHour());
		String segundo = String.valueOf(date.getSecond());
		if(date.getMonthValue() <= 9) {
			mes = +0+""+date.getMonthValue();
		}
		if(date.getMinute() <= 9) {
			minuto = +0+""+date.getMinute();
		}
		if(date.getHour() <= 9) {
			hora = +0+""+date.getHour();
		}
		if(date.getSecond() <= 9) {
			segundo = +0+""+date.getSecond();
		}
		String dataFormatada = date.getYear()+"-"+mes+"-"+date.getDayOfMonth()+" "+hora+":"+minuto+":"+segundo;
		return dataFormatada;
	}
	  
	  
}
