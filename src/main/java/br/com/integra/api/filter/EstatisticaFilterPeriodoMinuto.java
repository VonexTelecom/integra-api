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
	
	@ApiModelProperty(value = "Periodo da Estatística", dataType = "Enum", example = "CincoMinutos")
	private PeriodoEstatisticaEnum periodoEnum;	
	
}
