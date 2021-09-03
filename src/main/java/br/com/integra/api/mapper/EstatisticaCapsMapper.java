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
<<<<<<< HEAD

		System.out.println(caps.size());

		EstatisticaDiscadorOutputDto capsSainte = caps.stream().filter(c -> c.getTipoEstatistica().equals("max_caps_sainte")).findFirst().orElseGet(
				() -> EstatisticaDiscadorOutputDto.builder()
=======
		EstatisticaCapsOutputDto capsDto = new EstatisticaCapsOutputDto();
		
		List<EstatisticaDiscadorOutputDto> capsModificado = new ArrayList<>();
		capsModificado.addAll(caps);
		EstatisticaDiscadorOutputDto capsVazioMax = EstatisticaDiscadorOutputDto.builder()
>>>>>>> 1ecbc0659817f4e37e20509092082e2e7b941ebb
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("max_caps_sainte").build());


		EstatisticaDiscadorOutputDto chamadaDiscada = caps.stream().filter(c -> c.getTipoEstatistica().equals("chamadas_discadas")).findFirst().orElseGet( 
				() -> EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
<<<<<<< HEAD
				.tipoEstatistica("chamadas_discadas").build());
		
		List<EstatisticaDiscadorOutputDto> valores = new ArrayList<>();
		valores.add(capsSainte);
		valores.add(chamadaDiscada);
=======
				.tipoEstatistica("chamadas_discadas").build();
		
		if(caps.isEmpty()){
			capsModificado.add(capsVazioDiscadas);
			capsModificado.add(capsVazioMax);
			
		}else if(caps.size() < 2) {
			for(EstatisticaDiscadorOutputDto capsUnico : caps) {
				if(capsUnico.getTipoEstatistica().equals("max_caps_sainte")) {
					capsModificado.add(capsVazioDiscadas);
				}else {
					capsModificado.add(capsVazioMax);
				}
			}
		} 
			
		capsDto = EstatisticaCapsOutputDto.builder()
			.data(dataAtual)
			.valores(capsModificado)
			.build();
>>>>>>> 1ecbc0659817f4e37e20509092082e2e7b941ebb
		
		return EstatisticaCapsOutputDto.builder()
				.data(dataAtual)
				.valores(valores)
				.build();


	}
}
