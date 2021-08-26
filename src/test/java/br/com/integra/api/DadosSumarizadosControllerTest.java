package br.com.integra.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.integra.api.model.EstatisticaSumarizada;
import br.com.integra.api.repository.EstatisticaSumarizadaRepository;
import br.com.integra.api.service.EstatisticaSumarizadaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DadosSumarizadosControllerTest {

	@Autowired
	EstatisticaSumarizadaRepository estatisticaSumarizadaRepository;

	@Autowired
	EstatisticaSumarizadaService estatisticaSumarizadaService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void buscarEstatisticasSumarizadasSucesso() throws Exception {
		EstatisticaSumarizada estatisticaSumarizada = EstatisticaSumarizada.builder()
				.data(LocalDateTime.of(2021,8,25, 14, 7,39))
				.clienteId(1L)
				.chamadasDiscadas(new BigDecimal("0.00"))
				.chamadasCompletadas(new BigDecimal("0.00"))
				.chamadasCompletadas3Segundos(new BigDecimal("0.00"))
				.chamadasCompletadas10Segundos(new BigDecimal("0.00"))
				.chamadasCompletadas30Segundos(new BigDecimal("0.00"))
				.chamadasCompletadasMais30Segundos(new BigDecimal("0.00"))
				.build();
		EstatisticaSumarizada newEstatisticaSumarizada = estatisticaSumarizadaRepository.save(estatisticaSumarizada);

		assertThat(newEstatisticaSumarizada).isNotNull();
	}
}
