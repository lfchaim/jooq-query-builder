// src/main/java/com/querybuilder/model/query/QueryDefinition.java
package com.querybuilder.model.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QueryDefinition {

    @JsonProperty("query")
    private QueryRoot query;

    public QueryDefinition() {}

    public QueryRoot getQuery() { return query; }
    public void setQuery(QueryRoot query) { this.query = query; }

    // -------------------------------------------------------------------------
    // Inner class QueryRoot
    // -------------------------------------------------------------------------
    public static class QueryRoot {

        private String name;
        private MainQuery mainQuery;
        private List<SubQuery> subQueries;

        public QueryRoot() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public MainQuery getMainQuery() { return mainQuery; }
        public void setMainQuery(MainQuery mainQuery) { this.mainQuery = mainQuery; }

        public List<SubQuery> getSubQueries() { return subQueries; }
        public void setSubQueries(List<SubQuery> subQueries) { this.subQueries = subQueries; }

        @Override
        public String toString() {
            return "QueryRoot{name='" + name + "'}";
        }
    }

    @Override
    public String toString() {
        return "QueryDefinition{query=" + query + "}";
    }
}