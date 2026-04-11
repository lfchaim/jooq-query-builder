// src/main/java/com/querybuilder/model/dataset/DataSetDefinition.java
package com.querybuilder.model.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataSetDefinition {

    @JsonProperty("dataset-name")
    private String datasetName;

    @JsonProperty("jdbc-url")
    private String jdbcUrl;

    @JsonProperty("jdbc-usr")
    private String jdbcUsr;

    @JsonProperty("jdbc-crypto-pwd")
    private String jdbcCryptoPwd;

    public DataSetDefinition() {}

    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }

    public String getJdbcUrl() { return jdbcUrl; }
    public void setJdbcUrl(String jdbcUrl) { this.jdbcUrl = jdbcUrl; }

    public String getJdbcUsr() { return jdbcUsr; }
    public void setJdbcUsr(String jdbcUsr) { this.jdbcUsr = jdbcUsr; }

    public String getJdbcCryptoPwd() { return jdbcCryptoPwd; }
    public void setJdbcCryptoPwd(String jdbcCryptoPwd) { this.jdbcCryptoPwd = jdbcCryptoPwd; }

    @Override
    public String toString() {
        return "DataSetDefinition{datasetName='" + datasetName + "', jdbcUrl='" + jdbcUrl + "'}";
    }
}