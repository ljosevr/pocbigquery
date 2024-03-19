package org.falabella.pocbigquery.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.falabella.pocbigquery.entity.Vtakaowp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Service
@Slf4j
public class BigQueryServiceImpl implements BigQueryService{

	private final BigQuery bigQueryConfig;

	private final VtakaowpService vtakaowpService;


	@Value("${spring.cloud.gcp.bigquery.project-id}")
	private String projectId1;

	@Value("${spring.cloud.gcp.bigquery.dataset-name}")
	private String datasetName1;

	@Value("${spring.cloud.gcp.bigquery.tableName}")
	private String tableName1;


	@Autowired
	public BigQueryServiceImpl(
			BigQuery bigQueryConfig,
			VtakaowpService vtakaowpService) {
		this.bigQueryConfig = bigQueryConfig;
		this.vtakaowpService = vtakaowpService;
	}

	@Scheduled(cron = "0 */2 * * * *")
	@Async
	public void consulta() throws InterruptedException {
		log.info("Iniciando Proceso Migración");

		String query = getQuery1();
		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
		TableResult tableResult = bigQueryConfig.query(queryConfig);

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

		return "SELECT * FROM `" + projectId1+"."+datasetName1+"."+tableName1+"`";

//		return "SELECT " +
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
//				"FROM " + projectId1+"."+datasetName1+"."+tableName1 + " "+
//
//				" WHERE " +
//				" Fecha_actualizacion  ='"+ LocalDate.now().minusDays(1) + "' " +
//				" and Payment_status <>\"PENDING\" " +
//				" ORDER BY 1 , 5";
	}

	public Vtakaowp mapearRowToEntityVtakaowp(FieldValueList row){

		Vtakaowp vtakaowp =Vtakaowp.builder()
				.dproceso(getDateString(row.get(0).getNumericValue()))
				.dcreacion(getDateString(row.get(1).getNumericValue()))
				.status(row.get(2).getStringValue())
				.cvendedor(row.get(3).getLongValue())
//				.nomVendedor(row.get(4).getStringValue().equals("-") ? "": row.get(4).getStringValue())
//				.crut(row.get(5).getStringValue().equals("-") ? "": row.get(5).getStringValue())
				.nomVendedor(row.get(4).getStringValue())
				.crut(row.get(5).getStringValue())
				.nordencmp(row.get(6).getLongValue())
				.lineid(row.get(7).getStringValue())
				.invoicetype(row.get(8).getStringValue())
				.gidLvlNumber(row.get(9).getStringValue())
				.sku(row.get(10).getStringValue())
				.gidNameFull(row.get(11).getStringValue())
				.mventa(row.get(12).getDoubleValue())
				.mventaSdescto(row.get(13).getDoubleValue())
				.mneto(row.get(14).getDoubleValue())
				.dactualiza(LocalDate.parse(row.get(15).getStringValue()))
				.dcompra(LocalDate.parse(row.get(16).getStringValue()))
				.build();
		log.info("Vtakaowp: {}", vtakaowp);

		return vtakaowp;
	}

	private String getDateString(BigDecimal dateBigDecimal) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Convertir BigDecimal a segundos y nanosegundos
		long seconds = dateBigDecimal.longValue();
		int nanoSeconds = dateBigDecimal.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(1000000000)).intValue();

		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, nanoSeconds, ZoneOffset.UTC);

		return localDateTime.format(dateFormat);
	}
}
