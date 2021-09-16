package br.com.integra.api.dto.output;

import java.time.LocalDateTime;
import java.util.Date;
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
	private String data;
	
	private List<EstatisticaDiscadorOutputDto> valores;
}
