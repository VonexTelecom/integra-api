package br.com.integra.api.scheduling;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.integra.api.repository.CountRepository;

@Configuration
@EnableScheduling
public class VerificaData {
	
	@Autowired
	private CountRepository repository;
	
	private static final String TIME_ZONE = "America/Sao_Paulo";

	@Scheduled(cron = "5 * * * * *", zone = TIME_ZONE)
	public void verificadorData() {
		LocalDate data = LocalDate.now();
		repository.gerarTabelaEstatisticaDiscador(data.plusDays(1L));
		
		
	}
}
