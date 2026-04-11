// src/main/java/com/querybuilder/controller/QueryBuilderController.java
package com.querybuilder.controller;

import com.querybuilder.model.query.QueryDefinition;
import com.querybuilder.service.DataSetService;
import com.querybuilder.service.QueryBuilderService;
import com.querybuilder.service.QueryExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/query-builder")
public class QueryBuilderController {

    private static final Logger log = LoggerFactory.getLogger(QueryBuilderController.class);

    private final QueryBuilderService queryBuilderService;
    private final DataSetService dataSetService;
    private final QueryExecutorService queryExecutorService;

    public QueryBuilderController(QueryBuilderService queryBuilderService,
                                   DataSetService dataSetService,
                                   QueryExecutorService queryExecutorService) {
        this.queryBuilderService = queryBuilderService;
        this.dataSetService = dataSetService;
        this.queryExecutorService = queryExecutorService;
    }

    @GetMapping("/{dataSet}/{queryName}")
    public ResponseEntity<List<Map<String, Object>>> executeQuery(
            @PathVariable String dataSet,
            @PathVariable String queryName,
            @RequestParam Map<String, String> params) {

        log.info("Executando query '{}' no dataset '{}' com params: {}", queryName, dataSet, params);

        QueryDefinition queryDefinition = queryBuilderService.loadQuery(queryName);
        DataSource dataSource = dataSetService.getDataSource(dataSet);
        List<Map<String, Object>> result = queryExecutorService.execute(dataSource, queryDefinition, params);

        return ResponseEntity.ok(result);
    }
}