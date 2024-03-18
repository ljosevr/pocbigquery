package org.falabella.pocbigquery.service;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BigQueryService {

	private final BigQuery bigquery;

	private final PruebaService pruebaService;

	@Value("${spring.cloud.gcp.project-id}")
	private String projectId;

	@Value("${spring.cloud.gcp.bigquery.dataset-name}")
	private String datasetName;

	@Value("${spring.cloud.gcp.bigquery.table-name}")
	private String tableName;

	public BigQueryService(BigQuery bigquery, PruebaService pruebaService) {
		this.bigquery = bigquery;
		this.pruebaService = pruebaService;
	}

	@Scheduled(cron = "0 */2 * * * *")
	@Async
	public void consulta() throws InterruptedException {

		String query = "SELECT * FROM "+projectId+"."+datasetName+"."+tableName;

		QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
		TableResult tableResult = bigquery.query(queryConfig);

		for(FieldValueList row : tableResult.iterateAll()) {
			log.info("fila: {}", row.toString());
			pruebaService.insertarPrueba(row.get(0).getStringValue(), row.get(1).getStringValue());
		}

	}
}
