package br.com.integra.api.repository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.integra.api.countRowMapper;
import br.com.integra.api.model.CountEstatisticaDiscador;

@Repository
public class CountRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void gerarTabelaEstatisticaDiscador(LocalDate date) {
		
		String dataFormatada = formatarData(date);
		String nomeDaTabelaAtual = String.format("EstatisticaDiscadorDia%s", dataFormatada);
		boolean tabelaExiste = VerificaTabelaExistente(nomeDaTabelaAtual);
		if (tabelaExiste == false) {
			String sql = String.format("CREATE TABLE %s ("
					+ "data TIMESTAMP not null, tipoEstatistica varchar(60) not null,"
					+ "quantidade decimal(10) not null, numeroOrigem varchar(15) not null,"
					+ "equipamento varchar(255) not null, conta varchar (10) not null,"
					+ "clienteId varchar(11) not null, modalidade varchar(10) not null,"
					+ "tipoEstatiscaValor varchar(100) DEFAULT NULL,"
					+ "UNIQUE KEY %s_tipoEstatistica_segundo (tipoEstatistica,tipoEstatiscaValor,clienteId) USING BTREE)", nomeDaTabelaAtual,nomeDaTabelaAtual);

			jdbcTemplate.execute(sql);
		}
	}
	
	public boolean VerificaTabelaExistente(String nomeDaTabela) {
		String sql = String.format("SELECT COUNT(*) "
				+"FROM information_schema.tables "
				+"WHERE table_schema = 'IntegraBI' " 
				+"AND table_name = '%s'", nomeDaTabela);
		CountEstatisticaDiscador count = jdbcTemplate.queryForObject(sql, new countRowMapper());
		if (count.getCount() > 0) {
			return true;
		}
		return false;
	}
	

	public String formatarData(LocalDate date) {
		String mes = ""+date.getMonthValue();
		if(date.getMonthValue() <= 9) {
			mes = 0+""+date.getMonthValue();
		}
		String dataFormatada = date.getYear()+""+mes+""+date.getDayOfMonth();
		return dataFormatada;
	}
}
