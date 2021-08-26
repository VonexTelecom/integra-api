DROP TABLE IF EXISTS INTEGRA_BI.ESTATISTICASUMARIZADAMINUTO;

CREATE TABLE INTEGRA_BI.ESTATISTICASUMARIZADAMINUTO (
  data DATETIME PRIMARY KEY,
  cliente_id INTEGER PRIMARY KEY,
  chamadas_discadas DECIMAL(10,2) DEFAULT '0.00',
  chamadas_completadas DECIMAL(10,2) DEFAULT '0.00',
  chamadas_completadas_ate_3_segundos DECIMAL(10,2) DEFAULT '0.00',
  chamadas_completadas_ate_10_segundos DECIMAL(10,2) DEFAULT '0.00',
  chamadas_completadas_ate_30_segundos DECIMAL(10,2) DEFAULT '0.00',
  chamadas_completadas_com_mais_de_30_segundos DECIMAL(10,2) DEFAULT '0.00') 



