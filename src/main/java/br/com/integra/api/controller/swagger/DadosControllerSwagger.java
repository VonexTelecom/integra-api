package br.com.integra.api.controller.swagger;

import org.springframework.http.ResponseEntity;

import br.com.integra.api.exception.handler.Problem;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaSumarizada;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Controller da Estatistica")
public interface DadosControllerSwagger {

	@ApiOperation(value = "Busca as Estatísticas Sumarizadas pelos minutos", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizada.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>findAllPeriodoMinuto(EstatisticaFilter filter);
	
} 



