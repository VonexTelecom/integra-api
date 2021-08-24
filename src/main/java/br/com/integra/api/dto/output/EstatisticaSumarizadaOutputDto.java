package br.com.integra.api.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaSumarizadaOutputDto {
	
	private Long clienteId;
	
	private BigDecimal chamadasDiscadas;
	
	private BigDecimal chamadasCompletadas;
	
	private BigDecimal chamadasCompletadas3Segundos;
	
	private BigDecimal chamadasCompletadas10Segundos;
	
	private BigDecimal chamadasCompletadas30Segundos;
	
	private BigDecimal chamadasCompletadasMais30Segundos;
}
