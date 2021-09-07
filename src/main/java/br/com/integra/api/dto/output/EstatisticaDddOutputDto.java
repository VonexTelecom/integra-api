package br.com.integra.api.dto.output;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaDddOutputDto {
	
	private String Latitude;
	private String Longitude;
	
	private Integer ddd;
	
	private String local;
	
	@JsonProperty("total")
	private BigDecimal quantidade;
	
	

}
