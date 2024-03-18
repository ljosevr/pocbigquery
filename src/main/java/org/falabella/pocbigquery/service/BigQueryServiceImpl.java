package org.falabella.pocbigquery.service;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.falabella.pocbigquery.config.BigQueryConfig;
import org.falabella.pocbigquery.entity.Vtakaowp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@Slf4j
public class BigQueryServiceImpl implements BigQueryService{

	private final BigQueryConfig bigQueryConfig;

	private final VtakaowpService vtakaowpService;


	@Value("${spring.cloud.gcp.bigquery.project-id}")
	private String projectId1;

	@Value("${spring.cloud.gcp.bigquery.dataset-name}")
	private String datasetName1;

	@Value("${spring.cloud.gcp.bigquery.tableName}")
	private String tableName1;


	@Autowired
	public BigQueryServiceImpl(
			BigQueryConfig bigQueryConfig,
			VtakaowpService vtakaowpService) {
		this.bigQueryConfig = bigQueryConfig;
		this.vtakaowpService = vtakaowpService;
	}

	@Scheduled(cron = "0 */2 * * * *")
	@Async
	public void consulta() throws InterruptedException {
		log.info("Iniciando Proceso Migración");

		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(getQuery1()).build();
		TableResult tableResult = bigQueryConfig.bigQuery().query(queryConfig);

		for(FieldValueList row : tableResult.iterateAll()) {
			log.info("fila: {}", row.toString());
			vtakaowpService.insertar(mapearRowToEntityVtakaowp(row));
		}

		for(FieldValueList row : tableResult.iterateAll()) {
			log.info("fila2: {}", row.toString());
			vtakaowpService.insertar(mapearRowToEntityVtakaowp(row));
		}

	}

	private String getQuery1() {
		return "SELECT * " +
//				"Fecha_actualizacion, " +
//				"createdAt,--SUBSTR(createdAt, 0,10) createdAt," +
//				"Payment_status," +
//				"salesPersonId," +
//				"agentname," +
//				"agentid," +
//				"oc," +
//				"lineId," +
//				"invoiceType," +
//				"GCATEGORY," +
//				"(lineId) SKU," +
//				"displayName," +
//				"CAST(TOTAL_SIN_DESCUENTOS_ITEMS AS INT64) TOTAL_SIN_DESCUENTOS_ITEMS," +
//				"CAST (TOTAL_DESCUENTOS_ITEMS AS INT64) TOTAL_DESCUENTOS_ITEMS," +
//				"CAST (ROUND(TOTAL_DESPUES_IMPUESTOS_ITEMS, 0)AS INT64) TOTAL_DESPUES_IMPUESTOS_ITEMS," +
//				"FORMAT_DATETIME(\"%d-%m-%Y\", TIMESTAMP(Fecha_actualizacion)) Fecha_act," +
//				"FORMAT_DATETIME(\"%d-%m-%Y\", TIMESTAMP(Fecha_actualizacion)) Fecha_cre " +
//				"FROM " + projectId2+"."+datasetName2+"."+tableName2 + " "+
				"FROM `" + projectId1+"."+datasetName1+"."+tableName1 + "` "+
				"WHERE " +
				"Fecha_actualizacion  ='"+ LocalDate.now().minusDays(1) + "' " +
				"and Payment_status <>\"PENDING\" " +
				"ORDER BY 1 , 5";
	}

	public Vtakaowp mapearRowToEntityVtakaowp(FieldValueList row){
		return
		Vtakaowp.builder()
				.dproceso(row.get(1).getStringValue())
				.dcreacion(row.get(2).getStringValue())
				.status(row.get(3).getStringValue())
				.cvendedor(row.get(4).getLongValue())
				.nomVendedor(row.get(5).getStringValue())
				.crut(row.get(6).getStringValue())
				.nordencmp(row.get(7).getLongValue())
				.lineid(row.get(8).getStringValue())
				.invoicetype(row.get(9).getStringValue())
				.gidLvlNumber(row.get(10).getStringValue())
				.sku(row.get(11).getStringValue())
				.gidNameFull(row.get(12).getStringValue())
				.mventa(row.get(13).getDoubleValue())
				.mventaSdescto(row.get(14).getDoubleValue())
				.mneto(row.get(15).getDoubleValue())
				.dactualiza(LocalDate.parse(row.get(16).getStringValue()))
				.dcompra(LocalDate.parse(row.get(17).getStringValue()))
				.build();

	}
}
