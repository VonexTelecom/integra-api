package br.com.integra.api.dto.output;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaCapsOutputDto {
	
	private List<ValoresCapsOutputDto> chamadasDiscadas = new ArrayList<>();
	
	private List<ValoresCapsOutputDto> maxCapsSainte = new ArrayList<>();
}
