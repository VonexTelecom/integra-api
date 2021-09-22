package br.com.integra.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.EstatisticaSumarizada;

public class EstatisticaSumarizadaRowMapper implements RowMapper<EstatisticaSumarizada>{

	/**
	 *Classe para conversão de resultado de query para model
	 */
	@Override
	public EstatisticaSumarizada mapRow(ResultSet rs, int rowNum) throws SQLException {

			return EstatisticaSumarizada.builder()
				.clienteId(rs.getLong("clienteId"))
				.data(rs.getTimestamp("data").toLocalDateTime())
				.quantidade(rs.getBigDecimal("quantidade"))
				.tipoEstatistica(rs.getString("tipoEstatistica"))
				.tipoEstatisticaValor(rs.getString("tipoEstatisticaValor"))
				.build();

	}

}
