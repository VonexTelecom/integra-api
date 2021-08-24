package br.com.integra.api.filter;

import java.time.Instant;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaFilterPeriodoData {

	 
	@ApiModelProperty(value = "Id do Cliente", dataType = "Long", example = "1234", required = true)
	private Long cliente;
	@ApiModelProperty(value = "Data Inicial", dataType = "Date", example = "AAAA/MM/DD")
	private Date dataInicial;
	@ApiModelProperty(value = "Data Final", dataType = "Date", example = "AAAA/MM/DD")
	private Date dataFinal ;
}
