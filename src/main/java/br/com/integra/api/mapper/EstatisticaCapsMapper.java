package br.com.integra.api.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;

@Component
public class EstatisticaCapsMapper {

	public EstatisticaCapsOutputDto modelToOutputDto(List<EstatisticaDiscadorOutputDto> caps, LocalDateTime dataAtual) {
		EstatisticaCapsOutputDto capsDto = new EstatisticaCapsOutputDto();
		
		System.out.println(caps.size());
		List<EstatisticaDiscadorOutputDto> capsModificado = new ArrayList<>();
		capsModificado.addAll(caps);
		EstatisticaDiscadorOutputDto capsVazioMax = EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("max_caps_sainte").build();
		
		EstatisticaDiscadorOutputDto capsVazioDiscadas = EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
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
		
		return capsDto;
	}
}
