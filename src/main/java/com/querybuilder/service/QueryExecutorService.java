// src/main/java/com/querybuilder/service/QueryExecutorService.java
package com.querybuilder.service;

import com.querybuilder.model.query.*;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.*;

@Service
public class QueryExecutorService {

    private static final Logger log = LoggerFactory.getLogger(QueryExecutorService.class);

    public List<Map<String, Object>> execute(
            DataSource dataSource,
            QueryDefinition queryDefinition,
            Map<String, String> params) {

        DSLContext dsl = DSL.using(dataSource, SQLDialect.POSTGRES);
        MainQuery mainQuery = queryDefinition.getQuery().getMainQuery();

        SelectQuery<Record> select = buildSelect(
                dsl,
                mainQuery.getFrom(),
                mainQuery.getJoins(),
                mainQuery.getSelect(),
                mainQuery.getFilters(),
                params);

        log.debug("SQL principal: {}", select.getSQL());

        List<Map<String, Object>> results = new ArrayList<>();
        select.fetch().forEach(record -> results.add(new LinkedHashMap<>(record.intoMap())));

        System.out.println("results: "+results != null?results.size():-1);
        
        List<SubQuery> subQueries = queryDefinition.getQuery().getSubQueries();
        if (subQueries != null && !subQueries.isEmpty()) {
            for (SubQuery subQuery : subQueries) {
                executeSubQuery(dsl, subQuery, results, params);
            }
        }

        return results;
    }

    // -------------------------------------------------------------------------
    // SubQuery
    // -------------------------------------------------------------------------

    private void executeSubQuery(DSLContext dsl,
                                  SubQuery subQuery,
                                  List<Map<String, Object>> parentResults,
                                  Map<String, String> params) {

        for (Map<String, Object> parentRow : parentResults) {
            Object bindValue = parentRow.get(subQuery.getMainResultKey());
            if (bindValue == null) continue;

            Map<String, String> subParams = new HashMap<>(params);
            subParams.put(subQuery.getBindKey(), String.valueOf(bindValue));

            SelectQuery<Record> subSelect = buildSelect(
                    dsl,
                    subQuery.getFrom(),
                    subQuery.getJoins(),
                    subQuery.getSelect(),
                    subQuery.getFilters(),
                    subParams);

            log.debug("SQL subquery [{}]: {}", subQuery.getName(), subSelect.getSQL());

            List<Map<String, Object>> subResults = new ArrayList<>();
            subSelect.fetch().forEach(r -> subResults.add(new LinkedHashMap<>(r.intoMap())));

            System.out.println("subResults: "+subResults != null?subResults.size():-1);
            
            parentRow.put(subQuery.getMergeKey(), subResults);
        }
    }

    // -------------------------------------------------------------------------
    // Builder genérico de SELECT
    // -------------------------------------------------------------------------

    private SelectQuery<Record> buildSelect(DSLContext dsl,
                                             String from,
                                             List<JoinDefinition> joins,
                                             List<String> selectFields,
                                             List<FilterGroup> filterGroups,
                                             Map<String, String> params) {

        SelectQuery<Record> query = dsl.selectQuery();

        for (String field : selectFields) {
            query.addSelect(DSL.field(field));
        }

        query.addFrom(DSL.table(from));

        if (joins != null) {
            for (JoinDefinition join : joins) {
                Table<?> joinTable = DSL.table(join.getTable());
                Condition onCondition = DSL.condition(join.getOn());

                switch (join.getType().toUpperCase()) {
                    case "INNER" -> query.addJoin(joinTable, JoinType.JOIN, onCondition);
                    case "LEFT"  -> query.addJoin(joinTable, JoinType.LEFT_OUTER_JOIN, onCondition);
                    case "RIGHT" -> query.addJoin(joinTable, JoinType.RIGHT_OUTER_JOIN, onCondition);
                    case "FULL"  -> query.addJoin(joinTable, JoinType.FULL_OUTER_JOIN, onCondition);
                    default      -> query.addJoin(joinTable, JoinType.JOIN, onCondition);
                }
            }
        }

        if (filterGroups != null) {
            for (FilterGroup group : filterGroups) {
                if (!isGroupApplicable(group, params)) continue;

                for (FilterCondition cond : group.getConditions()) {
                    String paramValue = params.get(cond.getParam());
                    if (paramValue == null) continue;

                    Condition condition = buildCondition(cond.getColumn(), cond.getOp(), paramValue);
                    query.addConditions(condition);
                }
            }
        }

        return query;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private boolean isGroupApplicable(FilterGroup group, Map<String, String> params) {
        if (group.getRequired() == null || group.getRequired().isEmpty()) return true;
        return group.getRequired().stream().allMatch(params::containsKey);
    }

    private Condition buildCondition(String column, String op, String value) {
        Field<Object> field = DSL.field(column);
        Object typedValue = inferType(value);
        
        return switch (op) {
            case "="         -> field.eq(DSL.val(typedValue));
            case "!="        -> field.ne(DSL.val(typedValue));
            case ">"         -> field.gt(DSL.val(typedValue));
            case ">="        -> field.ge(DSL.val(typedValue));
            case "<"         -> field.lt(DSL.val(typedValue));
            case "<="        -> field.le(DSL.val(typedValue));
            case "LIKE"      -> field.like(value);
            case "NOT LIKE"  -> field.notLike(value);
            case "IN"        -> field.in(Arrays.stream(value.split(","))
                                    .map(String::trim).toArray());
            case "NOT IN"    -> field.notIn(Arrays.stream(value.split(","))
                                    .map(String::trim).toArray());
            case "IS NULL"      -> field.isNull();
            case "IS NOT NULL"  -> field.isNotNull();
            default -> DSL.condition("{0} " + op + " {1}", field, DSL.val(typedValue));
        };
    }
    
    /**
     * Tenta inferir o tipo Java mais adequado para o valor recebido como String.
     * Ordem: Long → Double → Boolean → String
     */
    private Object inferType(String value) {
        if (value == null) return null;

        // Tenta Long (cobre int, bigint)
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {}

        // Tenta Double (cobre numeric, float, decimal)
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {}

        // Tenta Boolean
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }

        // Tenta LocalDate (formato yyyy-MM-dd)
        try {
            return java.time.LocalDate.parse(value);
        } catch (java.time.format.DateTimeParseException ignored) {}

        // Tenta LocalDateTime (formato yyyy-MM-ddTHH:mm:ss)
        try {
            return java.time.LocalDateTime.parse(value);
        } catch (java.time.format.DateTimeParseException ignored) {}

        // Fallback: String
        return value;
    }
}