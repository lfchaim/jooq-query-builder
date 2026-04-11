// src/main/java/com/querybuilder/service/DataSetService.java
package com.querybuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querybuilder.model.dataset.DataSetDefinition;
import com.querybuilder.util.CryptoUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataSetService {

    private static final Logger log = LoggerFactory.getLogger(DataSetService.class);

    private final ObjectMapper objectMapper;
    private final CryptoUtil cryptoUtil;

    @Value("${query-builder.dataset-path}")
    private String datasetPath;

    private final Map<String, HikariDataSource> dataSourceCache = new ConcurrentHashMap<>();

    public DataSetService(ObjectMapper objectMapper, CryptoUtil cryptoUtil) {
        this.objectMapper = objectMapper;
        this.cryptoUtil = cryptoUtil;
    }

    public DataSource getDataSource(String datasetName) {
        return dataSourceCache.computeIfAbsent(datasetName, this::createDataSource);
    }

    private HikariDataSource createDataSource(String datasetName) {
        DataSetDefinition def = loadDataSetDefinition(datasetName);
        String plainPassword = cryptoUtil.decrypt(def.getJdbcCryptoPwd());

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(def.getJdbcUrl());
        config.setUsername(def.getJdbcUsr());
        config.setPassword(plainPassword);
        config.setPoolName("pool-" + datasetName);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);
        config.setMaxLifetime(1_800_000);

        log.info("Criando DataSource para dataset: {}", datasetName);
        return new HikariDataSource(config);
    }

    private DataSetDefinition loadDataSetDefinition(String datasetName) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(datasetPath + "*.json");

            for (Resource resource : resources) {
                DataSetDefinition def = objectMapper.readValue(resource.getInputStream(), DataSetDefinition.class);
                if (datasetName.equalsIgnoreCase(def.getDatasetName())) {
                    return def;
                }
            }
            throw new IllegalArgumentException("DataSet não encontrado: " + datasetName);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar DataSet: " + datasetName, e);
        }
    }

    @PreDestroy
    public void closeAll() {
        dataSourceCache.values().forEach(ds -> {
            if (!ds.isClosed()) ds.close();
        });
    }
}