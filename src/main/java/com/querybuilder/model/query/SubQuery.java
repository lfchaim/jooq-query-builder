// src/main/java/com/querybuilder/model/query/SubQuery.java
package com.querybuilder.model.query;

import java.util.List;

public class SubQuery {

    private String name;
    private String bindKey;
    private String mainResultKey;
    private String from;
    private List<JoinDefinition> joins;
    private List<String> select;
    private List<FilterGroup> filters;
    private String mergeKey;

    public SubQuery() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBindKey() { return bindKey; }
    public void setBindKey(String bindKey) { this.bindKey = bindKey; }

    public String getMainResultKey() { return mainResultKey; }
    public void setMainResultKey(String mainResultKey) { this.mainResultKey = mainResultKey; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public List<JoinDefinition> getJoins() { return joins; }
    public void setJoins(List<JoinDefinition> joins) { this.joins = joins; }

    public List<String> getSelect() { return select; }
    public void setSelect(List<String> select) { this.select = select; }

    public List<FilterGroup> getFilters() { return filters; }
    public void setFilters(List<FilterGroup> filters) { this.filters = filters; }

    public String getMergeKey() { return mergeKey; }
    public void setMergeKey(String mergeKey) { this.mergeKey = mergeKey; }

    @Override
    public String toString() {
        return "SubQuery{name='" + name + "', bindKey='" + bindKey + "', mergeKey='" + mergeKey + "'}";
    }
}