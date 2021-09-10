package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaSumarizada {
	private LocalTime data;
	private Long clienteId;
	
	private BigDecimal quantidade;
	private String tipoEstatistica;
	private String tipoEstatisticaValor;
	
}
