package br.com.integra.api.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EstatisticaDiscador {
	private LocalTime data;
	private String tipoEstatistica;
	private BigDecimal quantidade;
	private String numeroOrigem;
	private String equipamento;
	private String conta;
	private Integer clienteId;
	private String modalidade;
	private String tipoEstisticaValor;
}
	
