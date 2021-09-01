package br.com.integra.api.filter;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.integra.api.enums.ModalidadeEnum;
import br.com.integra.api.enums.PeriodoEstatisticaEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaFilter {

	@ApiModelProperty(name = "periodoEnum", value = "Periodo da Estatística", dataType = "Enum", example = "TrintaDias")
	private PeriodoEstatisticaEnum periodoEnum;	
	 
	@ApiModelProperty(name = "dataInicial", value = "A Data Início", required = false, position = 3, dataType = "DateTime", example = "2021-01-31T00:00")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date dataInicial;
	
	
	@ApiModelProperty(name = "dataFinal", value = "A Data Final", required = false, position = 3, dataType = "DateTime", example = "2021-01-31T23:59")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date dataFinal;
	
	@ApiModelProperty(name = "modalidade", value = "A Modalilade", dataType = "Enum", example = "fixo")
	private ModalidadeEnum modalidade;
	
}
