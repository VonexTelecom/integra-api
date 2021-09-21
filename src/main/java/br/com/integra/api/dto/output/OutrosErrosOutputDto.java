package br.com.integra.api.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutrosErrosOutputDto {
	
	private String statusChamada;
	private String descricao;	
	private BigDecimal quantidade;
}
