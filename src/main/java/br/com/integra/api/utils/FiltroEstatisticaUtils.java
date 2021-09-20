package br.com.integra.api.utils;

import br.com.integra.api.filter.EstatisticaFilter;

public class FiltroEstatisticaUtils {
	
	public static String criarQuery(String nomeDaTabelaData,String tipoEstatistica,
			EstatisticaFilter filter,Long clienteId,Integer digitoInicial,Integer digitoFinal
			, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("SELECT * FROM %s WHERE 0 = 0", nomeDaTabelaData));
		
		if(tipoEstatistica != null && tipoEstatistica.isEmpty() == false && !tipoEstatistica.equals("caps")) {
			sql.append(String.format(" AND tipoEstatistica = '%s'",tipoEstatistica));
		}else if(tipoEstatistica != null && tipoEstatistica.isEmpty() == false && tipoEstatistica.equals("caps")) {
			sql.append(" AND tipoEstatistica = 'max_caps_sainte' OR tipoEstatistica = 'chamadas_discadas'");
		}
		
		if(filter.getModalidade().size() != 0) {
			for(int i = 0; i < filter.getModalidade().size();i++) {
				if(i == 0) {
					sql.append((String)(" AND modalidade = '"+filter.getModalidade().get(i)+"'"));
				} else {
					sql.append((String)(" OR modalidade = '"+filter.getModalidade().get(i)+"'"));
				}
			}	
		}
		if(clienteId != null) {
			sql.append(String.format(" AND clienteId = %d", clienteId));
		}
		
		if(digitoInicial != null  && digitoFinal != null) {
			sql.append(String.format(" AND tipoEstatisticaValor between '%d' and '%d'",digitoInicial, digitoFinal));
		}
		
		if(filter.getOperadora().size() != 0) {
			
			for(int i = 0; i < filter.getOperadora().size() ;i++) {
				if(i == 0) {
					sql.append((String)(" AND operadora = '"+filter.getOperadora().get(i)+"'"));
				} else {
					sql.append((String)(" OR operadora = '"+filter.getOperadora().get(i)+"'"));
				}
			}	
		}
		
		if(filter.getDiscador().size() != 0) {
			for(int i = 0; i < filter.getDiscador().size() ;i++) {
				if(i == 0) {
					sql.append((String)(" AND discador = '"+filter.getDiscador().get(i)+"'"));
				} else {
					sql.append((String)(" OR discador = '"+filter.getDiscador().get(i)+"'"));
				}
			}
		}
		
		if(dataInicial != null && dataFinal != null) {
			sql.append(String.format(" AND data between '%s' and '%s'", dataInicial, dataFinal));
		}
		
		if(filter.getUnidadeAtendimento().size() > 0){
			for(int i = 0; i <filter.getUnidadeAtendimento().size();i++) {
				if(i == 0) {
					sql.append((String)(" AND unidadeAtendimento LIKE '%"+filter.getUnidadeAtendimento().get(i)+"%'"));
				} else {
					sql.append((String)(" OR unidadeAtendimento LIKE '%"+filter.getUnidadeAtendimento().get(i)+"%'"));
				}
			}	 
		} 
		return sql.toString();
	}
	
	public static String criarQueryOutrosErros(String nomeDaTabelaData,String tipoEstatistica,
			EstatisticaFilter filter,Long clienteId,Integer digitoInicial,Integer digitoFinal
			, String dataInicial, String dataFinal) {
		StringBuilder sql = new StringBuilder();
		sql.append(String.format("SELECT * FROM %s WHERE 0 = 0", nomeDaTabelaData));
				
		if(filter.getModalidade().size() != 0) {
			for(int i = 0; i < filter.getModalidade().size();i++) {
				if(i == 0) {
					sql.append((String)(" AND modalidade = '"+filter.getModalidade().get(i)+"'"));
				} else {
					sql.append((String)(" OR modalidade = '"+filter.getModalidade().get(i)+"'"));
				}
			}	
		}
		if(clienteId != null) {
			sql.append(String.format(" AND clienteId = %d", clienteId));
		}
				
		if(filter.getOperadora().size() != 0) {
			
			for(int i = 0; i < filter.getOperadora().size() ;i++) {
				if(i == 0) {
					sql.append((String)(" AND operadora = '"+filter.getOperadora().get(i)+"'"));
				} else {
					sql.append((String)(" OR operadora = '"+filter.getOperadora().get(i)+"'"));
				}
			}	
		}
		
		if(filter.getDiscador().size() != 0) {
			for(int i = 0; i < filter.getDiscador().size() ;i++) {
				if(i == 0) {
					sql.append((String)(" AND discador = '"+filter.getDiscador().get(i)+"'"));
				} else {
					sql.append((String)(" OR discador = '"+filter.getDiscador().get(i)+"'"));
				}
			}
		}
		
		if(dataInicial != null && dataFinal != null) {
			sql.append(String.format(" AND data between '%s' and '%s'", dataInicial, dataFinal));
		}
		
		if(filter.getUnidadeAtendimento().size() > 0){
			for(int i = 0; i <filter.getUnidadeAtendimento().size();i++) {
				if(i == 0) {
					sql.append((String)(" AND unidadeAtendimento LIKE '%"+filter.getUnidadeAtendimento().get(i)+"%'"));
				} else {
					sql.append((String)(" OR unidadeAtendimento LIKE '%"+filter.getUnidadeAtendimento().get(i)+"%'"));
				}
			}	 
		} 
		return sql.toString();
	}
	
}
