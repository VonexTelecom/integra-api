package br.com.integra.api.commom.serializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.com.integra.api.model.EstatisticaDiscador;

public class CustomListMaxCapsSainteSerializer extends JsonSerializer<List<EstatisticaDiscador>>{
	  @Override
	    public void serialize(List<EstatisticaDiscador> value, JsonGenerator jgen,
	            SerializerProvider provider) throws IOException{
	        jgen.writeStartArray(); 
	        
	        value.stream().filter(e -> e.getTipoEstatistica().equals("max_caps_sainte"))
	        	.forEach(x ->{
	        		 try {
						jgen.writeStartObject(); 
						jgen.writeObjectField("data", x.getData().atZone( ZoneId.ofOffset("GMT", ZoneOffset.UTC)).format(DateTimeFormatter.ISO_INSTANT)); 
						jgen.writeObjectField("quantidade", x.getQuantidade()); 
						jgen.writeEndObject();
					} catch (IOException e1) { 
						e1.printStackTrace();
					}
	        	});

	        jgen.writeEndArray();
	    }
}
