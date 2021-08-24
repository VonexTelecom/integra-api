package br.com.integra.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.integra.api.config.security.IntegraSecurity;
import br.com.integra.api.controller.swagger.DadosControllerSwagger;
import br.com.integra.api.filter.EstatisticaFilterPeriodoData;
import br.com.integra.api.filter.EstatisticaFilterPeriodoMinuto;
import br.com.integra.api.service.EstatisticaSumarizadaService;

@RestController
@RequestMapping("/estatistica-sumarizada")
public class DadosSumarizadosController implements DadosControllerSwagger {

	@Autowired
	private EstatisticaSumarizadaService service;
	
	@Autowired
	private IntegraSecurity integraSecurity;

	@Override
	@GetMapping("/periodo-por-minuto")
	public ResponseEntity<?> findAllPeriodoMinuto(EstatisticaFilterPeriodoMinuto filter) {

		
		return ResponseEntity.ok(service.findPorPeriodo(filter, integraSecurity.getClienteId()));

	}

	@Override
	@GetMapping("/periodo-por-data")
	public ResponseEntity<?> findAllPeriodoData(EstatisticaFilterPeriodoData filter) {

		return ResponseEntity.ok(service.findPorDatas(filter, integraSecurity.getClienteId()));
	}

}
