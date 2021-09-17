package br.com.integra.api.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.utils.DateUtils;


/**
 * @author Rafael Lopes
 *	
 *	classe para valição e conversão do model para dto 
 */
@Component
public class EstatisticaCapsMapper {

	/**
	 * @param caps (caps a ser processado)
	 * @param dataAtual (data do caps)
	 * @return estatisticaCapsOutputDto
	 */
	public EstatisticaCapsOutputDto modelToOutputDto(List<EstatisticaDiscadorOutputDto> caps, LocalDateTime dataAtual) {

		//instancia que percorre a lista(caps) e verifica se em algum deles falta um valor
		//caso sim, ele o acrescenta setado como zero 
		EstatisticaDiscadorOutputDto capsSainte = caps.stream().filter(c -> c.getTipoEstatistica().equals("max_caps_sainte")).findFirst().orElseGet(
				() -> EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("max_caps_sainte").build());

		EstatisticaDiscadorOutputDto chamadaDiscada = caps.stream().filter(c -> c.getTipoEstatistica().equals("chamadas_discadas")).findFirst().orElseGet( 
				() -> EstatisticaDiscadorOutputDto.builder()
				.quantidade(BigDecimal.ZERO)
				.tipoEstatistica("chamadas_discadas").build());
		
		List<EstatisticaDiscadorOutputDto> valores = new ArrayList<>();
		valores.add(chamadaDiscada);
		valores.add(capsSainte);
		EstatisticaCapsOutputDto estatisticaCaps = EstatisticaCapsOutputDto.builder()
				.data(dataAtual.format(DateTimeFormatter.ISO_DATE_TIME).toString())
				.valores(valores)
				.build();
		return estatisticaCaps;
	}
}
