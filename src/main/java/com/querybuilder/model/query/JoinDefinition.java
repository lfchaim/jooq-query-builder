// src/main/java/com/querybuilder/model/query/JoinDefinition.java
package com.querybuilder.model.query;

public class JoinDefinition {

    private String type;
    private String table;
    private String on;

    public JoinDefinition() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTable() { return table; }
    public void setTable(String table) { this.table = table; }

    public String getOn() { return on; }
    public void setOn(String on) { this.on = on; }

    @Override
    public String toString() {
        return "JoinDefinition{type='" + type + "', table='" + table + "', on='" + on + "'}";
    }
}