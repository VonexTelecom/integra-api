package br.com.integra.api.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "EstatisticaSumarizadaMinuto")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaSumarizadas {
	
	@Column(name = "data")
	private Date data;
	
	@Id
	@Column(name = "clienteId")
	private Long clienteId;
	
	@Column(name = "chamadas_discadas")
	private BigDecimal chamadasDiscadas;
	
	@Column(name = "chamadas_completadas")
	private BigDecimal chamadasCompletadas;
	
	@Column(name = "chamadas_completadas_ate_3_segundos")
	private BigDecimal chamadasComplementadas3Segundos;
	
	@Column(name = "chamadas_completadas_ate_10_segundos")
	private BigDecimal chamadasComplementadas10Segundos;
	
	@Column(name = "chamadas_completadas_ate_30_segundos")
	private BigDecimal chamadasComplementadas30Segundos;
	
	@Column(name = "chamadas_completadas_com_mais_de_30_segundos")
	private BigDecimal chamadasComplementadasMais30Segundos;
	
}
