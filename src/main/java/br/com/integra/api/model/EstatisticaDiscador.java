package br.com.integra.api.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EstatisticaDiscador {
	private Date data;
	private String tipoEstatistica;
	private BigDecimal quantidade;
	private String numeroOrigem;
	private String equipamento;
	private String conta;
	private Integer clienteId;
	private String modalidade;
	private String tipoEstisticaValor;
}
	
