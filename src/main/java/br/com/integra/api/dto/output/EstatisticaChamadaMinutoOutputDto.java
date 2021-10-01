package br.com.integra.api.dto.output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.integra.api.commom.serializer.CustomListChamadasCompletadasSerializer;
import br.com.integra.api.commom.serializer.CustomListChamadasDiscadasSerializer;
import br.com.integra.api.model.EstatisticaDiscador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaChamadaMinutoOutputDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("chamadas_discadas")
	@JsonSerialize(using = CustomListChamadasDiscadasSerializer.class)
	List<EstatisticaDiscador> chamadasDiscadas = new ArrayList<>();
	
	@JsonProperty("chamadas_completadas")
	@JsonSerialize(using = CustomListChamadasCompletadasSerializer.class)
	List<EstatisticaDiscador> chamadasCompletadas = new ArrayList<>();
	
	
}