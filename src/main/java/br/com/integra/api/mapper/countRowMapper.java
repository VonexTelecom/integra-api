package br.com.integra.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.integra.api.model.CountEstatisticaDiscador;

public class countRowMapper implements RowMapper<CountEstatisticaDiscador>{

	@Override
	public CountEstatisticaDiscador mapRow(ResultSet rs, int rowNum) throws SQLException {
		CountEstatisticaDiscador count = new CountEstatisticaDiscador();
		count.setCount(rs.getLong("COUNT(*)"));
		return count;
	}

}
