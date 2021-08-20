package br.com.integra.api.controller.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Pageable")
public class PageableSwagger {
	
	@ApiModelProperty(value = "Tamanho da página",  example = "10")
	private int size;
	
	@ApiModelProperty(value = "Numero da página",  example = "0")
	private int page;
}
