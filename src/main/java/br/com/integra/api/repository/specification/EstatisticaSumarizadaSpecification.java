package br.com.integra.api.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import br.com.integra.api.model.EstatisticaSumarizadas;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@Spec(path = "clienteId", params = "cliente", spec = Equal.class)
public interface EstatisticaSumarizadaSpecification extends Specification<EstatisticaSumarizadas>{

}
