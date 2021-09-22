package br.com.integra.api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import br.com.integra.api.enums.PeriodoEstatisticaEnum;

public class DateUtils {
	
	public static List<LocalDateTime> converterEnumToData(PeriodoEstatisticaEnum periodoEnum) {
		
		LocalDateTime dataAtual = LocalDateTime.now();
		LocalDateTime dataProcessada = LocalDateTime.from(dataAtual);
		LocalDateTime dataFinalProcessada = LocalDateTime.from(dataProcessada);
		
		List<LocalDateTime> datas = new ArrayList<>();
		
		
		switch (periodoEnum) {
		case Hoje:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay();
			dataFinalProcessada = dataAtual.toLocalDate().atTime(23, 59);
			break;
		case Ontem:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusDays(1);
			dataFinalProcessada =  LocalDateTime.now().toLocalDate().atTime(23,59);
			break;
		case QuinzeDias:
			
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusWeeks(2);
			dataFinalProcessada =LocalDateTime.now().toLocalDate().atTime(23,59);
			break;
		case TrintaDias:
			dataProcessada = dataAtual.toLocalDate().atStartOfDay().minusMonths(1);
			dataFinalProcessada = LocalDateTime.now().toLocalDate().atTime(23,59);
			break;
		case OitoAsDezoito:
			dataProcessada = dataAtual.toLocalDate().atTime(8, 0, 0);
			dataFinalProcessada = dataAtual.toLocalDate().atTime(18, 0, 0);
			break;
		default:
		}
		datas.add(dataProcessada);
		datas.add(dataFinalProcessada);
		
		return datas;
		
	}
	
	public static List<LocalDate> IntervaloData(
			LocalDate startDate, LocalDate endDate) { 
		long dias  = ChronoUnit.DAYS.between(startDate, endDate)+1; 
		
		return IntStream.iterate(0, i -> i + 1)
				.limit(dias)
				.mapToObj(i -> startDate.plusDays(i))
				.collect(Collectors.toList()); 
	}

	
	public static String formatarData(ZonedDateTime date) {
		
		List<String> dataSeparada = new ArrayList<>();
		String dataString =	date.format(DateTimeFormatter.RFC_1123_DATE_TIME).toString();
		String dataFormatada = "";
		dataString = dataString.concat(" ");
		dataString = dataString.replace(",", "");
		String d = "";
		for (char letra : dataString.toCharArray()) {
			if(letra == ' ') {
				
				dataSeparada.add(d);
				d = "";
			}else{
				d = d.concat(Character.toString(letra));
			}
		}
		dataFormatada = dataSeparada.get(0)+" "+dataSeparada.get(2)+" "+dataSeparada.get(1)+" "+dataSeparada.get(3)+" "+dataSeparada.get(4)+" "+date.getZone();
		return dataFormatada;
	} 
	
	//método para conversão de LocalDate(yyyy-MM-dd) para string(yyyyMMdd)
		public static String formatarData(LocalDate date) {

			return date.format(DateTimeFormatter.BASIC_ISO_DATE).toString();
		}
		//método para conversão de LocalTime para String
		public static String formatarData(LocalDateTime date) {	
			return date.format(DateTimeFormatter.ISO_DATE_TIME).toString();
		}
}
