package br.com.integra.api.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.integra.api.dto.output.EstatisticaDddOutputDto;

@Component
public class Coordenadas {
	public void getCoordenada(Map<Integer,EstatisticaDddOutputDto> coordenadas){
		
		coordenadas.put(11,EstatisticaDddOutputDto.builder()
				.local("São Paulo – SP")
				.Latitude("-23.50398")
				.Longetude("-46.6961687")
				.build());
		
		coordenadas.put(12,EstatisticaDddOutputDto.builder()
				.local("São José dos Campos – SP")
				.Latitude("-23.1900465")
				.Longetude("-45.8845542")
				.build());
		
		coordenadas.put(13,EstatisticaDddOutputDto.builder()
				.local("Santos – SP")
				.Latitude("-23.9475247")
				.Longetude("-46.3367247")
				.build());
		
		coordenadas.put(14,EstatisticaDddOutputDto.builder()
				.local("Bauru – SP")
				.Latitude("-22.2799593")
				.Longetude("-49.03234250000003")
				.build());
		
		coordenadas.put(15,EstatisticaDddOutputDto.builder()
				.local("Sorocaba – SP")
				.Latitude("-23.5311767")
				.Longetude("-47.4999302")
				.build());
		
		coordenadas.put(16,EstatisticaDddOutputDto.builder()
				.local("Ribeirão Preto – SP")
				.Latitude("-21.1704008")
				.Longetude("-47.8103238")
				.build());
		
		coordenadas.put(17,EstatisticaDddOutputDto.builder()
				.local("São José do Rio Preto – SP")
				.Latitude("-20.8296359")
				.Longetude("-49.3919963")
				.build());
		
		coordenadas.put(18,EstatisticaDddOutputDto.builder()
				.local("Presidente Prudente – SP")
				.Latitude("-22.1084400")
				.Longetude("-51.4020909")
				.build());
		
		coordenadas.put(19,EstatisticaDddOutputDto.builder()
				.local("Campinas – SP")
				.Latitude("-22.9096542")
				.Longetude("-47.1182323")
				.build());
		
		coordenadas.put(21,EstatisticaDddOutputDto.builder()
				.local("Rio de Janeiro – RJ")
				.Latitude("-22.89508")
				.Longetude("-43.54647799999998")
				.build());
		
		coordenadas.put(22,EstatisticaDddOutputDto.builder()
				.local("Campos dos Goytacazes – RJ")
				.Latitude("-21.7231772")
				.Longetude("-41.3034409")
				.build());
		
		coordenadas.put(24,EstatisticaDddOutputDto.builder()
				.local("Volta Redonda – RJ")
				.Latitude("-22.5392320")
				.Longetude("-44.0698799")
				.build());
		
		coordenadas.put(27,EstatisticaDddOutputDto.builder()
				.local("Vila Velha/Vitória – ES")
				.Latitude("-20.3819622")
				.Longetude("-40.3249251")
				.build());
		
		coordenadas.put(28,EstatisticaDddOutputDto.builder()
				.local("Cachoeiro de Itapemirim – ES")
				.Latitude("-20.8524181")
				.Longetude("-41.1527824")
				.build());
		
		coordenadas.put(31,EstatisticaDddOutputDto.builder()
				.local("Belo Horizonte – MG")
				.Latitude("-19.8765925")
				.Longetude("-43.9089138")
				.build());
		
		coordenadas.put(32,EstatisticaDddOutputDto.builder()
				.local("Juiz de Fora – MG")
				.Latitude("-21.7135331")
				.Longetude("-43.4086961")
				.build());
		
		coordenadas.put(33,EstatisticaDddOutputDto.builder()
				.local("Governador Valadares – MG")
				.Latitude("-18.8549317")
				.Longetude("-41.9559233")
				.build());	
		
		coordenadas.put(34,EstatisticaDddOutputDto.builder()
				.local("Uberlândia – MG")
				.Latitude("-18.9075379")
				.Longetude("-48.2276491")
				.build());
		
		coordenadas.put(35,EstatisticaDddOutputDto.builder()
				.local("Poços de Caldas – MG")
				.Latitude("-21.7804590")
				.Longetude("-46.5696245")
				.build());
		
		coordenadas.put(37,EstatisticaDddOutputDto.builder()
				.local("Divinópolis – MG")
				.Latitude("-20.1451261")
				.Longetude("-44.8916447")
				.build());
		
		coordenadas.put(38,EstatisticaDddOutputDto.builder()
				.local("Montes Claros – MG")
				.Latitude("-16.7524388")
				.Longetude("-43.8508865")
				.build());
		
		coordenadas.put(42,EstatisticaDddOutputDto.builder()
				.local("Ponta Grossa – PR")
				.Latitude("-25.0994246")
				.Longetude("-50.1583223")
				.build());
		
		coordenadas.put(41,EstatisticaDddOutputDto.builder()
				.local("Curitiba – PR")
				.Latitude("-25.4289541")
				.Longetude("-49.2671370")
				.build());
		
		coordenadas.put(43,EstatisticaDddOutputDto.builder()
				.local("Londrina – PR")
				.Latitude("-23.3044524")
				.Longetude("-51.1695824")
				.build());
		
		coordenadas.put(44,EstatisticaDddOutputDto.builder()
				.local("Maringá – PR")
				.Latitude("-23.3801915")
				.Longetude("-51.9574228")
				.build());
		
		coordenadas.put(45,EstatisticaDddOutputDto.builder()
				.local("Foz do Iguaçú – PR")
				.Latitude("-25.5469")
				.Longetude("-54.5882")
				.build());
		
		coordenadas.put(46,EstatisticaDddOutputDto.builder()
				.local("Francisco Beltrão/Pato Branco – PR")
				.Latitude("-26.0821584")
				.Longetude("-53.0539739")
				.build());
		
		coordenadas.put(47,EstatisticaDddOutputDto.builder()
				.local("Joinville – SC")
				.Latitude("-26.3495167")
				.Longetude("-48.8206799")
				.build());
		
		coordenadas.put(48,EstatisticaDddOutputDto.builder()
				.local("Florianópolis – SC")
				.Latitude("-27.5792924")
				.Longetude("-48.5316184")
				.build());
		
		coordenadas.put(49,EstatisticaDddOutputDto.builder()
				.local("Chapecó – SC")
				.Latitude("-27.0846339")
				.Longetude("-52.6000298")
				.build());
		
		coordenadas.put(51,EstatisticaDddOutputDto.builder()
				.local("Porto Alegre – RS")
				.Latitude("-30.1471445")
				.Longetude("-51.1376714")
				.build());
		
		coordenadas.put(53,EstatisticaDddOutputDto.builder()
				.local("Pelotas – RS")
				.Latitude("-31.7136182")
				.Longetude("-52.3558867")
				.build());
		
		coordenadas.put(54,EstatisticaDddOutputDto.builder()
				.local("Caxias do Sul – RS")
				.Latitude("-29.1315901")
				.Longetude("-51.1947430")
				.build());
		
		coordenadas.put(55,EstatisticaDddOutputDto.builder()
				.local("Santa Maria – RS")
				.Latitude("-29.6919911")
				.Longetude("-53.8561834")
				.build());
		
		coordenadas.put(61,EstatisticaDddOutputDto.builder()
				.local("Brasília – DF")
				.Latitude("-15.7825543")
				.Longetude("-47.8825263")
				.build());
		
		coordenadas.put(62,EstatisticaDddOutputDto.builder()
				.local("Goiânia – GO")
				.Latitude("-16.6645242")
				.Longetude("-49.3165750")
				.build());
		
		coordenadas.put(63,EstatisticaDddOutputDto.builder()
				.local("Palmas – TO")
				.Latitude("-10.1824903")
				.Longetude("-48.3375021")
				.build());
		
		coordenadas.put(64,EstatisticaDddOutputDto.builder()
				.local("Rio Verde – GO")
				.Latitude("-17.8133544")
				.Longetude("-50.9362460")
				.build());
		
		coordenadas.put(65,EstatisticaDddOutputDto.builder()
				.local("Cuiabá – MT")
				.Latitude("-15.6328845")
				.Longetude("-56.0090314")
				.build());
		
		coordenadas.put(66,EstatisticaDddOutputDto.builder()
				.local("Rondonópolis – MT")
				.Latitude("-16.4616978")
				.Longetude("-54.6213688")
				.build());
		
		coordenadas.put(67,EstatisticaDddOutputDto.builder()
				.local("Campo Grande – MS")
				.Latitude("-20.4490448")
				.Longetude("-54.6299613")
				.build());
		
		coordenadas.put(68,EstatisticaDddOutputDto.builder()
				.local("Rio Branco – AC")
				.Latitude("-9.948678900000024")
				.Longetude("-67.82728283491821")
				.build());
		
		coordenadas.put(69,EstatisticaDddOutputDto.builder()
				.local("Porto Velho – RO")
				.Latitude("-8.7442811")
				.Longetude("-63.9036845")
				.build());
		
		coordenadas.put(71,EstatisticaDddOutputDto.builder()
				.local("Salvador – BA")
				.Latitude("-12.9169143")
				.Longetude("-38.4771398")
				.build());
		
		coordenadas.put(73,EstatisticaDddOutputDto.builder()
				.local("Ilhéus – BA")
				.Latitude("-14.7935051")
				.Longetude("-39.0463797")
				.build());
		
		coordenadas.put(74,EstatisticaDddOutputDto.builder()
				.local("Juazeiro – BA")
				.Latitude("-9.4166382")
				.Longetude("-40.5036161")
				.build());
		
		coordenadas.put(75,EstatisticaDddOutputDto.builder()
				.local("Feira de Santana – BA")
				.Latitude("-12.2245399")
				.Longetude("-38.9537612")
				.build());
		
		coordenadas.put(77,EstatisticaDddOutputDto.builder()
				.local("Barreiras – BA")
				.Latitude("-14.2350040")
				.Longetude("-51.9252800")
				.build());
		
		coordenadas.put(79,EstatisticaDddOutputDto.builder()
				.local("Aracaju – SE")
				.Latitude("-10.8924935")
				.Longetude("-37.0963683")
				.build());
		
		coordenadas.put(81,EstatisticaDddOutputDto.builder()
				.local("Recife – PE")
				.Latitude("-8.0363325")
				.Longetude("-34.9529117")
				.build());
		
		coordenadas.put(82,EstatisticaDddOutputDto.builder()
				.local("Maceió – AL")
				.Latitude("-9.6522111")
				.Longetude("-35.7112229")
				.build());
		
		coordenadas.put(83,EstatisticaDddOutputDto.builder()
				.local("João Pessoa – PB")
				.Latitude("-7.1194958")
				.Longetude("-34.8450118")
				.build());
		
		coordenadas.put(84,EstatisticaDddOutputDto.builder()
				.local("Natal – RN")
				.Latitude("-5.7291840")
				.Longetude("-35.2724889")
				.build());
		
		coordenadas.put(85,EstatisticaDddOutputDto.builder()
				.local("Fortaleza – CE")
				.Latitude("-3.817661304479021")
				.Longetude("-38.58909533439328")
				.build());
		
		coordenadas.put(86,EstatisticaDddOutputDto.builder()
				.local("Teresina – PI")
				.Latitude("-5.0920108")
				.Longetude("-42.8037597")
				.build());
		
		coordenadas.put(87,EstatisticaDddOutputDto.builder()
				.local("Petrolina – PE")
				.Latitude("-9.3931632")
				.Longetude("-40.5171952")
				.build());

		coordenadas.put(88,EstatisticaDddOutputDto.builder()
				.local("Juazeiro do Norte – CE")
				.Latitude("-7.2290365")
				.Longetude("-39.3124461")
				.build());
		
		coordenadas.put(89,EstatisticaDddOutputDto.builder()
				.local("Picos – PI")
				.Latitude("-7.0776163")
				.Longetude("-41.4673741")
				.build());
		
		coordenadas.put(91,EstatisticaDddOutputDto.builder()
				.local("Belém – PA")
				.Latitude("-1.45583")
				.Longetude("-48.50444")
				.build());
		
		coordenadas.put(92,EstatisticaDddOutputDto.builder()
				.local("Manaus – AM")
				.Latitude("-3.0760558")
				.Longetude("-60.0367337")
				.build());
		
		coordenadas.put(93,EstatisticaDddOutputDto.builder()
				.local("Santarém – PA")
				.Latitude("-2.43944")
				.Longetude("-54.6987")
				.build());
		
		coordenadas.put(94,EstatisticaDddOutputDto.builder()
				.local("Marabá – PA")
				.Latitude("-5.2815143")
				.Longetude("-49.0786004")
				.build());
		
		coordenadas.put(95,EstatisticaDddOutputDto.builder()
				.local("Boa Vista – RR")
				.Latitude("2.8223103")
				.Longetude("-60.7043626")
				.build());
		
		coordenadas.put(96,EstatisticaDddOutputDto.builder()
				.local("Macapá – AP")
				.Latitude("0.0473278")
				.Longetude("-51.0465398")
				.build());
		
		coordenadas.put(97,EstatisticaDddOutputDto.builder()
				.local("Coari – AM")
				.Latitude("-4.0878386")
				.Longetude("-63.141209")
				.build());
		
		
		coordenadas.put(98,EstatisticaDddOutputDto.builder()
				.local("São Luís – MA")
				.Latitude("-2.5470084")
				.Longetude("-44.2845207")
				.build());
		
		coordenadas.put(99,EstatisticaDddOutputDto.builder()
				.local("Imperatriz – MA")
				.Latitude("-5.5049660")
				.Longetude("-47.4577087")
				.build());
	}
}	
