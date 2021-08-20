package br.com.integra.api.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.integra.api.model.EstatisticaSumarizadas;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public interface EstatisticaSumarizadaSpecification extends Specification<EstatisticaSumarizadas>{


}
