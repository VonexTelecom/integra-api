package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaDiscador {
	private LocalDateTime data;
	private String tipoEstatistica;
	private BigDecimal quantidade;
	private String numeroOrigem;
	private String equipamento;
	private String conta;
	private Integer clienteId;
	private String modalidade;
	private String tipoEstisticaValor;
	private String operadora;
	private String unidadeAtendimento;
	private String discador;
}
	
