package br.com.integra.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.integra.api.controller.swagger.DadosControllerSwagger;
import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.dto.output.EstatisticaSumarizadaPeriodoOutputDto;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.filter.EstatisticaFilterPeriodoData;
import br.com.integra.api.filter.EstatisticaFilterPeriodoMinuto;
import br.com.integra.api.repository.specification.EstatisticaSumarizadaSpecification;
import br.com.integra.api.service.EstatisticaSumarizadaService;

@RestController
@RequestMapping("/Estatistica-sumarizada")
public class DadosSumarizadosController implements DadosControllerSwagger{

	
	@Autowired
	private EstatisticaSumarizadaService service;
	
	@Override
	@GetMapping
	public ResponseEntity<Page<EstatisticaSumarizadaOutputDto>> findAll(
			EstatisticaSumarizadaSpecification spec, Pageable pageable, EstatisticaFilter filter){
		Page page = service.findAll(spec, pageable);
		return ResponseEntity.ok(page);
	}

	@Override
	@GetMapping("/periodo-por-minuto")
	public ResponseEntity<?> findAllPeriodoMinuto(EstatisticaSumarizadaSpecification spec,
			EstatisticaFilterPeriodoMinuto filter) {
		EstatisticaSumarizadaPeriodoOutputDto estatisticasPeriodo = service.FindAllPeriodo(spec, filter);
		return ResponseEntity.ok(estatisticasPeriodo);
		
	}

	@Override
	@GetMapping("/periodo-por-data")
	public ResponseEntity<?> findAllPeriodoData(EstatisticaSumarizadaSpecification spec,
			EstatisticaFilterPeriodoData filter) {
		EstatisticaSumarizadaPeriodoOutputDto estatisticasPeriodo = service.FindAllPeridoData(spec, filter);
		return ResponseEntity.ok(estatisticasPeriodo);
	}
	
}
