package br.com.integra.api.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaFilterPeriodoData {

	 
	@ApiModelProperty(name = "dataInicial", value = "A Data Início", required = false, position = 3, dataType = "Date", example = "01/01/2020")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;
	
	@ApiModelProperty(name = "dataFinal", value = "A Data Final", required = false, position = 3, dataType = "Date", example = "01/01/2020")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;
}
