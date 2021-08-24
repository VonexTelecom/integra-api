package br.com.integra.api.controller.swagger;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.integra.api.exception.handler.Problem;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.filter.EstatisticaFilterPeriodoData;
import br.com.integra.api.filter.EstatisticaFilterPeriodoMinuto;
import br.com.integra.api.model.EstatisticaSumarizadas;
import br.com.integra.api.repository.specification.EstatisticaSumarizadaSpecification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Controller da Estatistica")
public interface DadosControllerSwagger {

	@ApiOperation(value = "Busca um Estatistica Sumarizada", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizadas.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>findAll(
			EstatisticaSumarizadaSpecification spec, Pageable pageable,  EstatisticaFilter filter);
	
	@ApiOperation(value = "Busca as Estatisticas Sumarizadas pelos minutos", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizadas.class, message = "Requisição com sucesso"),
		@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>findAllPeriodoMinuto(
			EstatisticaSumarizadaSpecification spec, EstatisticaFilterPeriodoMinuto filter);
	

	@ApiOperation(value = "Busca as Estatisticas Sumarizadas pela data", httpMethod = "GET")
	@ApiResponses({
		@ApiResponse(code = 200, response = EstatisticaSumarizadas.class, message = "Requisição com sucesso"),
	@ApiResponse(code = 404, response = Problem.class, message = "O recurso não foi encontrado")
	})
	ResponseEntity<?>findAllPeriodoData(
			EstatisticaSumarizadaSpecification spec, EstatisticaFilterPeriodoData filter);

} 



