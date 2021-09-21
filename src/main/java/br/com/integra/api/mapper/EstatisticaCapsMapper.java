package br.com.integra.api.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaCapsOutputDto;
import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.dto.output.ValoresCapsOutputDto;
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
	public List<ValoresCapsOutputDto> modelToOutputDto(List<EstatisticaDiscadorOutputDto> caps, LocalDateTime dataAtual) {

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
		
		List<ValoresCapsOutputDto> valores = new ArrayList<>();
		if(capsSainte.getQuantidade().intValue() != 0 || chamadaDiscada.getQuantidade().intValue() != 0) {
			ValoresCapsOutputDto capsSainteRetorno = ValoresCapsOutputDto.builder()
				.data(dataAtual.toString())
				.quantidade(capsSainte.getQuantidade()).build();
		ValoresCapsOutputDto chamadaDiscadaRetorno = ValoresCapsOutputDto.builder()
				.data(dataAtual.toString())
				.quantidade(chamadaDiscada.getQuantidade()).build();
		
		
		valores.add(capsSainteRetorno);
		valores.add(chamadaDiscadaRetorno);
		}
		return valores;
	}
}
