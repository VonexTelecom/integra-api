package br.com.integra.api.utils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
@Component
public class SumarizacaoEstatisticaSumarizada {
	
	public BigDecimal chamadasDiscadasTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasDiscadas())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	public BigDecimal chamadasCompletadasTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasCompletadas())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	public BigDecimal chamadasCompletadas3SegundosTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasCompletadas3Segundos())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	
	public BigDecimal chamadasCompletadas10SegundosTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasCompletadas10Segundos())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	
	public BigDecimal chamadasCompletadas30SegundosTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasCompletadas30Segundos())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	
	public BigDecimal chamadasCompletadasMais30SegundosTotal(List<EstatisticaSumarizadaOutputDto> estatisticas) {
		if(estatisticas.size() <=0) {
			return BigDecimal.ZERO;
		}else {
			return estatisticas
					.stream()
					.map(estatistica -> estatistica.getChamadasCompletadasMais30Segundos())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
	}
	
	
	
	
}
