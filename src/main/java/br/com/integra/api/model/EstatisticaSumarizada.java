package br.com.integra.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Entity
@Table(name = "EstatisticaSumarizadaMinuto", schema = "IntegraBI")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EstatisticaSumarizada {
	
	@Id
	@Column(name = "data")
	private LocalDateTime data;
	
	@Column(name = "cliente_id")
	private Long clienteId;
	
	@Column(name = "chamadas_discadas")
	private BigDecimal chamadasDiscadas;
	
	@Column(name = "chamadas_completadas")
	private BigDecimal chamadasCompletadas;
	
	@Column(name = "chamadas_completadas_ate_3_segundos")
	private BigDecimal chamadasCompletadas3Segundos;
	
	@Column(name = "chamadas_completadas_ate_10_segundos")
	private BigDecimal chamadasCompletadas10Segundos;
	
	@Column(name = "chamadas_completadas_ate_30_segundos")
	private BigDecimal chamadasCompletadas30Segundos;
	
	@Column(name = "chamadas_completadas_com_mais_de_30_segundos")
	private BigDecimal chamadasCompletadasMais30Segundos;
	
}
