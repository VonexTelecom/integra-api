package br.com.integra.api.controller.swagger;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.dto.output.OutrosErrosOutputDto;
import br.com.integra.api.exception.handler.Problem;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaSumarizada;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Controller da Estatistica")
public interface DadosControllerSwagger {

	@ApiOperation(value = "Busca as Estatísticas Sumarizadas por periodo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizada.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>findAllPeriodo(EstatisticaFilter filter);
	
	@ApiOperation(value = "Busca as estatísticas das chamadas por periodo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaDiscadorOutputDto.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 204, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>discadorTotalizadorChamadas(EstatisticaFilter filter);
	
	@ApiOperation(value = "Busca as estatísticas da duração da chamadas por periodo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaDiscadorOutputDto.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 204, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>chamadasTotalizadorSegundos(EstatisticaFilter filter);
	
	
	
	@ApiOperation(value = "Busca da estatística do total de ddds periodo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaDiscadorOutputDto.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 204, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>discadorTotalizadorDdd(EstatisticaFilter filter);
	
	@ApiOperation(value = "Busca da estatística do total de Chamadas Por Segundo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaCapsOutputDto.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 204, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>discadorTotalizadorCaps(EstatisticaFilter filter);

	@ApiOperation(value = "Busca os Outros Erros por periodo", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizada.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<Page<OutrosErrosOutputDto>> findAllOutrosErros(Pageable pageable, EstatisticaFilter filter);
	
} 



