// src/main/java/com/querybuilder/model/query/MainQuery.java
package com.querybuilder.model.query;

import java.util.List;

public class MainQuery {

    private String from;
    private List<JoinDefinition> joins;
    private List<String> select;
    private List<FilterGroup> filters;

    public MainQuery() {}

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public List<JoinDefinition> getJoins() { return joins; }
    public void setJoins(List<JoinDefinition> joins) { this.joins = joins; }

    public List<String> getSelect() { return select; }
    public void setSelect(List<String> select) { this.select = select; }

    public List<FilterGroup> getFilters() { return filters; }
    public void setFilters(List<FilterGroup> filters) { this.filters = filters; }

    @Override
    public String toString() {
        return "MainQuery{from='" + from + "', select=" + select + "}";
    }
}