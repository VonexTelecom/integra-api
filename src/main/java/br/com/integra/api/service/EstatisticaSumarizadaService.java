package br.com.integra.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.integra.api.dto.output.EstatisticaSumarizadaOutputDto;
import br.com.integra.api.mapper.EstatisticaSumarizadaMapper;
import br.com.integra.api.model.EstatisticaSumarizadas;
import br.com.integra.api.repository.EstatisticaSumarizadaRepository;

@Service
public class EstatisticaSumarizadaService {
	
	@Autowired
	private EstatisticaSumarizadaRepository repository;
	
	@Autowired
	private EstatisticaSumarizadaMapper mapper;
	
	
	public Page<EstatisticaSumarizadaOutputDto> findAll(Specification<EstatisticaSumarizadas> spec, Pageable pageable) {	
		
		Page<EstatisticaSumarizadas> page =  repository.findAll(spec, pageable);	
		return page.map(user -> mapper.modelToOutputDto(user));
	}
		
}
