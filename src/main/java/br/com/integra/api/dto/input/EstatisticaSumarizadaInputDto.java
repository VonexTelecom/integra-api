package br.com.integra.api.dto.input;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class EstatisticaSumarizadaInputDto {
	
	private Long clienteId;
	
	private Date data;
	
	private BigDecimal chamadasDiscadas;
	
	private BigDecimal chamadasCompletadas;
	
	private BigDecimal chamadasComplementadas3Segundos;
	
	private BigDecimal chamadasComplementadas10Segundos;
	
	private BigDecimal chamadasComplementadas30Segundos;
	
	private BigDecimal chamadasComplementadasMais30Segundos;

}
