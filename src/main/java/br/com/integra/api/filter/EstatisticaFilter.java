package br.com.integra.api.filter;


import java.time.Instant;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "EstatisticaSumarizadaFilter")
public class EstatisticaFilter {

	@ApiModelProperty(value = "Id do Cliente", dataType = "Long", example = "1234")
	private Long cliente;
	
}
