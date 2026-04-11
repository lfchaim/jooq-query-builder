// src/main/java/com/querybuilder/service/QueryBuilderService.java
package com.querybuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querybuilder.model.query.QueryDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QueryBuilderService {

    private static final Logger log = LoggerFactory.getLogger(QueryBuilderService.class);

    private final ObjectMapper objectMapper;

    @Value("${query-builder.query-path}")
    private String queryPath;

    public QueryBuilderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public QueryDefinition loadQuery(String queryName) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource(queryPath + queryName + ".json");

            if (!resource.exists()) {
                throw new IllegalArgumentException("Query não encontrada: " + queryName);
            }

            log.debug("Carregando query: {}", queryName);
            return objectMapper.readValue(resource.getInputStream(), QueryDefinition.class);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar query: " + queryName, e);
        }
    }
}