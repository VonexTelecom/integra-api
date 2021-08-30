package br.com.integra.api.filter;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.integra.api.enums.ModalidadeEnum;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaFilter {

	@ApiModelProperty(name = "periodoEnum", value = "Periodo da Estatística", dataType = "Enum", example = "TrintaDias")
	private PeriodoEstatisticaEnum periodoEnum;	
	 
	@ApiModelProperty(name = "dataInicial", value = "A Data Início", required = false, position = 3, dataType = "Date", example = "2021-01-01")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dataInicial;
	
	@ApiModelProperty(name = "dataFinal", value = "A Data Final", required = false, position = 3, dataType = "Date", example = "2021-01-31")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dataFinal;
	
	@ApiModelProperty(name = "modalidade", value = "A Modalilade", dataType = "Enum", example = "fixo")
	private ModalidadeEnum modalidade;
	
}
