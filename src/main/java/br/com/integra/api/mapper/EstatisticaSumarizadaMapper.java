package br.com.integra.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.integra.api.dto.input.EstatisticaSumarizadaInputDto;
import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
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
}
