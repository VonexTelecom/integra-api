package br.com.integra.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstatisticaSumarizadaEnum {
	chamadas_discadas(1L, "chamadas_discadas"),
	chamadas_completadas(2L, "chamadas_completadas"),
	chamadas_completadas_ate_3_segundos(3L,"chamadas_completadas_ate_3_segundos"),
	chamadas_completadas_ate_10_segundos(4L, "chamadas_completadas_ate_10_segundos"),
	chamadas_completadas_ate_30_segundos(5L, "chamadas_completadas_ate_30_segundos"),
	chamadas_completadas_com_mais_de_30_segundos(6L, "chamadas_completadas_com_mais_de_30_segundos");

	Long index;
	String valor;

	public static String getById(Long id) {
		for (EstatisticaSumarizadaEnum e : values()) {
			if (e.index.equals(id)) return e.getValor();
		}
		return null;
	}
}
