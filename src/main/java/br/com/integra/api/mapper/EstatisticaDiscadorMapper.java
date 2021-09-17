package br.com.integra.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaDiscadorOutputDto;
import br.com.integra.api.model.EstatisticaDiscador;

/**
 * @author Rafael Lopes 
 *
 *	Classe para conversão de model para dto
 */
@Component
public class EstatisticaDiscadorMapper {

	@Autowired
	private ModelMapper mapper;
	
	public EstatisticaDiscadorOutputDto modelToOutputDto(EstatisticaDiscador estatistica) {
		return mapper.map(estatistica, EstatisticaDiscadorOutputDto.class);
	}
	
	public EstatisticaDiscadorOutputDto modelToOutputDtoSegundo(EstatisticaDiscador estatistica) {
		EstatisticaDiscadorOutputDto dto = mapper.map(estatistica, EstatisticaDiscadorOutputDto.class);
		dto.setTipoEstatistica(formatarSegundoTabela(estatistica.getTipoEstatistica(), Integer.valueOf(estatistica.getTipoEstisticaValor())));
		return dto;
	}
	
	public EstatisticaDiscadorOutputDto modelToOutputDtoSegundoDDD(EstatisticaDiscador estatistica) {
		EstatisticaDiscadorOutputDto dto = mapper.map(estatistica, EstatisticaDiscadorOutputDto.class);
		dto.setTipoEstatistica(formatarTabelaDDD(estatistica.getTipoEstatistica(), Integer.valueOf(estatistica.getTipoEstisticaValor())));
		return dto;
	}
	
	public List<EstatisticaDiscadorOutputDto> modelToCollectionOutputDto(List<EstatisticaDiscador> estatisticas){
		return estatisticas.stream().map(estatistica -> modelToOutputDto(estatistica)).collect(Collectors.toList());
	}
	public String formatarSegundoTabela(String nomeTipoEstatica, Integer segundo) {
		String slice1 = nomeTipoEstatica.substring("chamada_com_".length(),nomeTipoEstatica.length());
		String slice2 = nomeTipoEstatica.substring(0,"chamada_com_".length());
		return slice2+segundo+"_"+slice1;
	}	
	
	public String formatarTabelaDDD(String nomeTipoEstatica, Integer ddd) {
		String slice1 = nomeTipoEstatica.substring("chamada_com_".length(),nomeTipoEstatica.length());
		String slice2 = nomeTipoEstatica.substring(0,"chamada_com_".length());
		return slice2+"_"+ddd+slice1;
	}	
}
