package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OutrosErros {
	private LocalTime data;
	private BigDecimal quantidade;
	private String numeroOrigem;	
	private String status_chamada;
	private String equipamento;
	private String conta;
	private Long clienteId;
	private String modalidade;
	private String operadora;
	private String unidadeAtendimento;
	private String discador;
}
	
