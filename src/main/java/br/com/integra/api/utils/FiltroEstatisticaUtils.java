package br.com.integra.api.utils;

import br.com.integra.api.filter.EstatisticaFilter;

public class FiltroEstatisticaUtils {
	
	public static String criarQuery(String nomeDaTabelaData,String tipoEstatistica,
			EstatisticaFilter filter,Long clienteId,Integer digitoInicial,Integer digitoFinal
			, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(String.format("SELECT * FROM %s WHERE 0 = 0", nomeDaTabelaData));
		
		if(tipoEstatistica.isEmpty() == false && !tipoEstatistica.equals("caps")) {
			sql.append(String.format(" AND tipoEstatistica = '%s'",tipoEstatistica));
		}else if(tipoEstatistica.isEmpty() == false && tipoEstatistica.equals("caps")) {
			sql.append(" AND tipoEstatistica = 'max_caps_sainte' OR tipoEstatistica = 'chamadas_discadas'");
		}
		if(filter.getModalidade() != null) {
			sql.append(String.format(" AND modalidade = '%s'",filter.getModalidade()));
		}
		if(clienteId != null) {
			sql.append(String.format(" AND clienteId = %d", clienteId));
		}
		if(digitoInicial != null  && digitoFinal != null) {
			sql.append(String.format(" AND tipoEstatisticaValor between '%d' and '%d'",digitoInicial, digitoFinal));
		}
		if(filter.getOperadora() != null) {
			sql.append(String.format(" AND operadora = '%s'", filter.getOperadora()));
		}
		if(filter.getDiscador() != null) {
			sql.append(String.format(" AND discador = '%s'", filter.getDiscador()));
		}
		if(dataInicial != null && dataFinal != null) {
			sql.append(String.format(" AND data between '%s' and '%s'", dataInicial, dataFinal));
		}
		if(filter.getUnidadeAtendimento() != null && filter.getUnidadeAtendimento().isBlank() == false) {
			sql.append((String)(" AND unidadeAtendimento LIKE '%"+filter.getUnidadeAtendimento()+"%'"));
		}
		
		return sql.toString();
	}
}
