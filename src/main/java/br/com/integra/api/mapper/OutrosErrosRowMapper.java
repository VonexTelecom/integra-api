package br.com.integra.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.OutrosErros;

public class OutrosErrosRowMapper implements RowMapper<OutrosErros>{

	/**
	 *Classe para conversão de resultado de query para model
	 */
	@Override
	public OutrosErros mapRow(ResultSet rs, int rowNum) throws SQLException {

			return OutrosErros.builder()
				.clienteId(rs.getLong("clienteId"))
				.data(rs.getTimestamp("data").toLocalDateTime())
				.quantidade(rs.getBigDecimal("quantidade"))
				.status_chamada(rs.getString("status_chamada"))
				.numeroOrigem(rs.getString("numeroOrigem"))
				.conta(rs.getString("conta"))
				.equipamento(rs.getString("equipamento"))
				.discador(rs.getString("discador"))
				.operadora(rs.getString("operadora"))
				.unidadeAtendimento(rs.getString("unidadeAtendimento"))
				.modalidade(rs.getString("modalidade"))
				.descricao(rs.getString("descricao"))
				.build();
	}

}
