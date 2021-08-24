package br.com.integra.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.model.EstatisticaSumarizada;

@Component
public class EstatisticaSumarizadaMapper {
	
	@Autowired
	private ModelMapper mapper;
	
	public EstatisticaSumarizadaOutputDto modelToOutputDto(EstatisticaSumarizada model) {
		return mapper.map(model, EstatisticaSumarizadaOutputDto.class);
	}
	
	public List<EstatisticaSumarizadaOutputDto> modelToCollectionOutputDto(List<EstatisticaSumarizada> models){
		return models.stream().map(model -> modelToOutputDto(model)).collect(Collectors.toList());
	}	
	
}
