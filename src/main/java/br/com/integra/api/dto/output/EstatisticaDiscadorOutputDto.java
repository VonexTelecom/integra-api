package br.com.integra.api.dto.output;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaDiscadorOutputDto {
	
	private String tipoEstatistica;
	@JsonProperty("total")
	private BigDecimal quantidade;
}
