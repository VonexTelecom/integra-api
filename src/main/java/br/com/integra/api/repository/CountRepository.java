package br.com.integra.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.mapper.CountRowMapper;
import br.com.integra.api.model.CountEstatisticaDiscador;

/**
 * @author Rafael Lopes
 * classe para a verificação de existencia da tabela chamada pelo repositório
 */

@Repository
public class CountRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param nomeDaTabela
	 * @return boolean(true para existencia, false para inexistencia)
	 */
	public boolean VerificaTabelaExistente(String nomeDaTabela) {
		String sql = String.format("SELECT COUNT(*) "
				+"FROM information_schema.tables "
				+"WHERE table_schema = 'IntegraBI' " 
				+"AND table_name = '%s'", nomeDaTabela);
		CountEstatisticaDiscador count = jdbcTemplate.queryForObject(sql, new CountRowMapper());
		if (count.getCount() > 0) {
			return true;
		}
		return false;
	}
}
