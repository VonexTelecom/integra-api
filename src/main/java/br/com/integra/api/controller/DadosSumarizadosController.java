package br.com.integra.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.integra.api.config.security.CheckSecurity;
import br.com.integra.api.config.security.IntegraSecurity;
import br.com.integra.api.controller.swagger.DadosControllerSwagger;
import br.com.integra.api.filter.EstatisticaFilter;
import br.com.integra.api.service.EstatisticaSumarizadaService;

@RestController
@RequestMapping("/estatistica-sumarizada")
public class DadosSumarizadosController implements DadosControllerSwagger {

	@Autowired
	private EstatisticaSumarizadaService service;
	
	@Autowired
	private IntegraSecurity integraSecurity;

	@Override
	@CheckSecurity.DadosSumarizados.PodeAcessar
	@GetMapping("/periodo")
	public ResponseEntity<?> findAllPeriodoMinuto(EstatisticaFilter filter) {
		
		return ResponseEntity.ok(service.findPorPeriodo(filter, integraSecurity.getClienteId()));
	}
}
