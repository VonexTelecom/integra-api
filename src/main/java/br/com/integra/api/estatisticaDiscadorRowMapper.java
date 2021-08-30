package br.com.integra.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.EstatisticaDiscador;

public class estatisticaDiscadorRowMapper implements RowMapper<EstatisticaDiscador>{

	@Override
	public EstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {

			return EstatisticaDiscador.builder()
				.clienteId(rs.getInt("clienteId"))
				.conta(rs.getString("conta"))
				.data(rs.getDate("data"))
				.equipamento(rs.getString("equipamento"))
				.modalidade(rs.getString("modalidade"))
				.numeroOrigem(rs.getString("numeroOrigem"))
				.quantidade(rs.getBigDecimal("quantidade"))
				.tipoEstatistica(rs.getString("tipoEstatistica"))
				.tipoEstisticaValor(rs.getString("tipoEstatiscaValor"))
				.build();

	}

}
