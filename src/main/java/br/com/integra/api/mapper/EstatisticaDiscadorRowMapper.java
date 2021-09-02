package br.com.integra.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.EstatisticaDiscador;

public class EstatisticaDiscadorRowMapper implements RowMapper<EstatisticaDiscador>{

	@Override
	public EstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {

			return EstatisticaDiscador.builder()
				.clienteId(rs.getInt("clienteId"))
				.conta(rs.getString("conta"))
				.data(rs.getTimestamp("data"))
				.equipamento(rs.getString("equipamento"))
				.modalidade(rs.getString("modalidade"))
				.numeroOrigem(rs.getString("numeroOrigem"))
				.quantidade(rs.getBigDecimal("quantidade"))
				.tipoEstatistica(rs.getString("tipoEstatistica"))
				.tipoEstisticaValor(rs.getString("tipoEstatiscaValor"))
				.build();

	}

}