package br.com.integra.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.EstatisticaDiscador;
import br.com.integra.api.model.EstatisticaSumarizada;

public class EstatisticaDiscadorRowMapper implements RowMapper<EstatisticaDiscador>{

	/**
	 *Classe para conversão de resultado de query para model
	 */
	@Override
	public EstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {

			return EstatisticaDiscador.builder()
				.clienteId(rs.getInt("clienteId"))
				.conta(rs.getString("conta"))
				.data(rs.getTime("data").toLocalTime())
				.equipamento(rs.getString("equipamento"))
				.modalidade(rs.getString("modalidade"))
				.numeroOrigem(rs.getString("numeroOrigem"))
				.quantidade(rs.getBigDecimal("quantidade"))
				.tipoEstatistica(rs.getString("tipoEstatistica"))
				.tipoEstisticaValor(rs.getString("tipoEstatisticaValor"))
				.operadora(rs.getString("operadora"))
				.discador(rs.getString("discador"))
				.unidadeAtendimento(rs.getString("unidadeAtendimento"))
				.build();

	}

}