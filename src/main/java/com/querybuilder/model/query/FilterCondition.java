// src/main/java/com/querybuilder/model/query/FilterCondition.java
package com.querybuilder.model.query;

public class FilterCondition {

    private String column;
    private String op;
    private String param;

    public FilterCondition() {}

    public String getColumn() { return column; }
    public void setColumn(String column) { this.column = column; }

    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }

    public String getParam() { return param; }
    public void setParam(String param) { this.param = param; }

    @Override
    public String toString() {
        return "FilterCondition{column='" + column + "', op='" + op + "', param='" + param + "'}";
    }
}