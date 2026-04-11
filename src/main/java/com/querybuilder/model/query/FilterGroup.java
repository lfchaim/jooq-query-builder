// src/main/java/com/querybuilder/model/query/FilterGroup.java
package com.querybuilder.model.query;

import java.util.List;

public class FilterGroup {

    private String groupId;
    private List<String> required;
    private List<String> optional;
    private List<FilterCondition> conditions;

    public FilterGroup() {}

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public List<String> getRequired() { return required; }
    public void setRequired(List<String> required) { this.required = required; }

    public List<String> getOptional() { return optional; }
    public void setOptional(List<String> optional) { this.optional = optional; }

    public List<FilterCondition> getConditions() { return conditions; }
    public void setConditions(List<FilterCondition> conditions) { this.conditions = conditions; }

    @Override
    public String toString() {
        return "FilterGroup{groupId='" + groupId + "', required=" + required + "}";
    }
}