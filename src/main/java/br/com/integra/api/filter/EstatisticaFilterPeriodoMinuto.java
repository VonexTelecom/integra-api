package br.com.integra.api.filter;

import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaFilterPeriodoMinuto {
	
	@ApiModelProperty(value = "Id do Cliente", dataType = "Long", example = "1234", required = true)
	private Long cliente;
	
	@ApiModelProperty(value = "Periodo da Estatistica", dataType = "String", example = "123")
	private PeriodoEstatisticaEnum periodoEnum;
	
	
}
