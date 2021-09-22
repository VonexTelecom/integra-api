package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaSumarizada {
	private LocalDateTime data;
	private Long clienteId;
	
	private BigDecimal quantidade;
	private String tipoEstatistica;
	private String tipoEstatisticaValor;
	
}
