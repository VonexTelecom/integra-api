package br.com.integra.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.integra.api.dto.input.EstatisticaSumarizadaInputDto;
import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.dto.output.EstatisticaSumarizadaPeriodoOutputDto;
import br.com.integra.api.model.EstatisticaSumarizadas;

@Component
public class EstatisticaSumarizadaMapper {
	@Autowired
	private ModelMapper mapper;
	
	public EstatisticaSumarizadaOutputDto modelToOutputDto(EstatisticaSumarizadas model) {
		return mapper.map(model, EstatisticaSumarizadaOutputDto.class);
	}
	
	public EstatisticaSumarizadas inputDtoToModel(EstatisticaSumarizadaInputDto request) {
		return mapper.map(request, EstatisticaSumarizadas.class);
	}
	
	public List<EstatisticaSumarizadaOutputDto> modelToCollectionOutputDto(List<EstatisticaSumarizadas> models){
		return models.stream().map(model -> modelToOutputDto(model)).collect(Collectors.toList());
	}
	
	public EstatisticaSumarizadaPeriodoOutputDto periodoModelToOutputDto(EstatisticaSumarizadas model) {
		return mapper.map(model, EstatisticaSumarizadaPeriodoOutputDto.class);
	}
}
