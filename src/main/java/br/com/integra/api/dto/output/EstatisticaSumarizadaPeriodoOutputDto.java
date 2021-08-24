package br.com.integra.api.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaSumarizadaPeriodoOutputDto {

	private Long clienteId;
	
	private BigDecimal chamadasDiscadas;
	
	private BigDecimal chamadasCompletadas;
	
	private BigDecimal chamadasComplementadas3Segundos;
	
	private BigDecimal chamadasComplementadas10Segundos;
	
	private BigDecimal chamadasComplementadas30Segundos;
	
	private BigDecimal chamadasComplementadasMais30Segundos;
}
