package br.com.integra.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum PeriodoEstatisticaEnum {
	CincoMinutos(0L, "5 Minutos"),
	DezMinutos(1L, "10 Minutos"),
	TrintaMinutos(2L, "30 Minutos");
	
	public static PeriodoEstatisticaEnum getById(Long id) {
		for (PeriodoEstatisticaEnum e : values()) {
			if (e.id.equals(id)) return e;
		}
		return null;
	}

	Long id;
	String descricao;
}
