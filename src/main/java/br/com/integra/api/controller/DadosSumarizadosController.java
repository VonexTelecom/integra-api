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
import br.com.integra.api.service.EstatisticaDiscadorChamadasService;
import br.com.integra.api.service.EstatisticaSumarizadaService;
import br.com.integra.api.service.EstatisticaTempoChamadaService;

@RestController
@RequestMapping("/estatistica-sumarizada")
public class DadosSumarizadosController implements DadosControllerSwagger {

	@Autowired
	private EstatisticaSumarizadaService service;
	
	@Autowired
	private EstatisticaDiscadorChamadasService totalizadorService;
	
	@Autowired
	private EstatisticaTempoChamadaService chamadaService;
	
	@Autowired
	private IntegraSecurity integraSecurity;

	@Override
	@CheckSecurity.DadosSumarizados.PodeAcessar
	@GetMapping("/periodo")
	public ResponseEntity<?> findAllPeriodo(EstatisticaFilter filter) {
		
		return ResponseEntity.ok(service.findPorPeriodo(filter, integraSecurity.getClienteId()));
	}

	@Override
	@CheckSecurity.DadosSumarizados.PodeAcessar
	@GetMapping("/discador-totalizador-dia")
	public ResponseEntity<?> discadorTotalizador(EstatisticaFilter filter) {
		
		return ResponseEntity.ok(totalizadorService.discadorTotalizadorChamadas(filter, integraSecurity.getClienteId()));
	}

	@Override
	@CheckSecurity.DadosSumarizados.PodeAcessar
	@GetMapping("/discador-duracao-chamadas-dia")
	public ResponseEntity<?> discadorTotalizadorQuantidadeSegundos(EstatisticaFilter filter) {
		
		return ResponseEntity.ok(chamadaService.discadorTotalizadorTempoChamadas(filter, integraSecurity.getClienteId()));
	}
	
	
}
