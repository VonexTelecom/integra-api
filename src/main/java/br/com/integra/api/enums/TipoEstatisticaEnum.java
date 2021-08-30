package br.com.integra.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEstatisticaEnum {
	chamadas_discadas(1L, "chamadas_discadas"),
	chamadas_desconectadas_discador_possibilidade_caixa_portal(21L, "chamadas_desconectadas_discador_possibilidade_caixa_portal"),
	chamadas_desconectadas_discador(3L,"chamadas_desconectadas_discador"),
	chamadas_completadas_ate_3_segundos(4L,"chamadas_completadas_ate_3_segundos"),
	chamadas_ate_3_segundos_desconectadas_pela_origem(4L,"chamadas_ate_3_segundos_desconectadas_pela_origem"),
	chamadas_ate_3_segundos_desconectadas_pelo_destino(5L, "chamadas_ate_3_segundos_desconectadas_pelo_destino"),
	chamadas_completadas_mais_3_segundos(6L, "chamadas_completadas_mais_3_segundos"),
	chamadas_mais_3_segundos_desconectadas_pela_origem(7L, "chamadas_mais_3_segundos_desconectadas_pela_origem"),
	chamadas_mais_3_segundos_desconectadas_pelo_destino(8L, "chamadas_mais_3_segundos_desconectadas_pelo_destino"),
	chamadas_numero_invalido(9L, "chamadas_numero_invalido"),
	chamadas_mensagem_operadora(10L, "chamadas_mensagem_operadora"),
	chamadas_outros_erros(11L, "chamadas_outros_erros"),
	chamadas_completadas_com_mais_de_30_segundos(12L, "chamadas_completadas_com_mais_de_30_segundos"),
	chamadas_completadas_com_mais_de_30_segundos_desc_origem(13L, "chamadas_completadas_com_mais_de_30_segundos_desc_origem"),
	chamadas_completadas_com_mais_de_30_segundos_desc_destino(14L, "chamadas_completadas_com_mais_de_30_segundos_desc_destino"),
	chamadas_completadas_ate_30_segundos(15L, "chamadas_completadas_ate_30_segundos"),
	chamadas_completadas_ate_30_segundos_desc_origem(16L, "chamadas_completadas_ate_30_segundos_desc_origem"),
	chamadas_completadas_ate_30_segundos_desc_destino(17L, "chamadas_completadas_ate_30_segundos_desc_destino"),
	chamadas_completadas_ate_10_segundos(18L, "chamadas_completadas_ate_10_segundos"),
	chamadas_ate_10_segundos_desconectadas_pela_origem(19L, "chamadas_ate_10_segundos_desconectadas_pela_origem"),
	chamadas_ate_10_segundos_desconectadas_pelo_destino(20L, "chamadas_ate_10_segundos_desconectadas_pelo_destino");
	Long index;
	
	String valor;

	public static String getById(Long id) {
		for (TipoEstatisticaEnum e : values()) {
			if (e.index.equals(id)) return e.getValor();
		}
		return null;
	}


}
