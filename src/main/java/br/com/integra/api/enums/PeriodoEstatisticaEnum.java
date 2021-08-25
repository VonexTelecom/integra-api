package br.com.integra.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum PeriodoEstatisticaEnum {
	Hoje(0L, "Hoje"),
	Ontem(1L, "Ontem"),
	QuinzeDias(2L, "15 Dias"),
	TrintaDias(3L, "30 Dias"),
	OitoAsDezoito(4L, "08:00 as 18:00");
	
	public static PeriodoEstatisticaEnum getById(Long id) {
		for (PeriodoEstatisticaEnum e : values()) {
			if (e.id.equals(id)) return e;
		}
		return null;
	}

	Long id;
	String descricao;
}
