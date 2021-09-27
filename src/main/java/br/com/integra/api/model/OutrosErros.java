package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OutrosErros {
	private LocalDateTime data;
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
	private String descricao;
}
	
