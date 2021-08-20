package br.com.integra.api.dto.output;

import java.math.BigDecimal;
import java.util.Date;

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
	
	private Date data;
	
	private BigDecimal chamadasDiscadas;
	
	private BigDecimal chamadasCompletadas;
	
	private BigDecimal chamadasComplementadas3Segundos;
	
	private BigDecimal chamadasComplementadas10Segundos;
	
	private BigDecimal chamadasComplementadas30Segundos;
	
	private BigDecimal chamadasComplementadasMais30Segundos;
}
