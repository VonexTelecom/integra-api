package br.com.integra.api.dto.output;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValoresCapsOutputDto {
	private String data;
	
	@JsonProperty("total")
	private BigDecimal quantidade;
}
