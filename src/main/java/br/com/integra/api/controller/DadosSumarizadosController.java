package br.com.integra.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.integra.api.controller.swagger.DadosControllerSwagger;
import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.model.EstatisticaSumarizadas;
import br.com.integra.api.service.EstatisticaSumarizadaService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@RequestMapping("/Estatistica-sumarizada")
public class DadosSumarizadosController implements DadosControllerSwagger{

	
	@Autowired
	private EstatisticaSumarizadaService service;
	
	@Override
	@GetMapping
	public ResponseEntity<Page<EstatisticaSumarizadaOutputDto>> findAll(
			@Spec(path = "ClienteId", params = "ClienteId", spec = Equal.class)
	Specification<EstatisticaSumarizadas> spec, Pageable pageable, EstatisticaFilter filter){
		Page page = service.findAll(spec, pageable);
		return ResponseEntity.ok(page);
	}


	
}
