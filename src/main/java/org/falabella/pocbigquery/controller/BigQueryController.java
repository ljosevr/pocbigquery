package org.falabella.pocbigquery.controller;

import lombok.extern.slf4j.Slf4j;
import org.falabella.pocbigquery.service.BigQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
public class BigQueryController {


    private final BigQueryService bigQueryService;

    public BigQueryController(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @GetMapping("/start")
    String start() throws InterruptedException {
        log.info("Iniciando Proceso Migración");
        bigQueryService.consulta();
        log.info("Proceso Migración Finalizado");
        return "Proceso Migración Finalizado";
    }
}
