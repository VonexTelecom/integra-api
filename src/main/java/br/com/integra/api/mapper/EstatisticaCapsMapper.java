package br.com.integra.api.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;

@Component
public class EstatisticaCapsMapper {

	public EstatisticaCapsOutputDto modelToOutputDto(List<EstatisticaDiscadorOutputDto> caps, LocalDateTime dataAtual) {

		System.out.println(caps.size());

		EstatisticaDiscadorOutputDto capsSainte = caps.stream().filter(c -> c.getTipoEstatistica().equals("max_caps_sainte")).findFirst().orElseGet(
				() -> EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("max_caps_sainte").build());


		EstatisticaDiscadorOutputDto chamadaDiscada = caps.stream().filter(c -> c.getTipoEstatistica().equals("chamadas_discadas")).findFirst().orElseGet( 
				() -> EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("chamadas_discadas").build());
		
		List<EstatisticaDiscadorOutputDto> valores = new ArrayList<>();
		valores.add(capsSainte);
		valores.add(chamadaDiscada);
		
		return EstatisticaCapsOutputDto.builder()
				.data(dataAtual)
				.valores(valores)
				.build();


	}
}
