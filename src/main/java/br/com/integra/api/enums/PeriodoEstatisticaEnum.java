package br.com.integra.api.enums;

import lombok.Getter;


@Getter
public enum PeriodoEstatisticaEnum {
	CINCO_MINUTOS(5L, "5 Minutos"),
	DEZ_MINUTOS(10L, "10 Minutos"),
	TRINTA_MINUTOS(30L, "30 Minutos");
	
	Long index;
	
	String descricao;
	
	private PeriodoEstatisticaEnum(Long i, String valor) {
		this.index = i;
		this.descricao = valor;
	}
}
