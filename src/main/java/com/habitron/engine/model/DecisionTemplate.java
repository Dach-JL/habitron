package com.habitron.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A reusable template for a decision.
 * Contains pre-defined options and criteria.
 */
public class DecisionTemplate {
    private String id;
    private String name;
    private List<Option> options;
    private List<Criterion> criteria;

    public DecisionTemplate() {
        this.options = new ArrayList<>();
        this.criteria = new ArrayList<>();
    }

    public DecisionTemplate(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public void addOption(Option o) {
        if (options.size() < DecisionSession.MAX_OPTIONS) {
            options.add(o);
        }
    }

    public void addCriterion(Criterion c) {
        if (criteria.size() < DecisionSession.MAX_CRITERIA) {
            criteria.add(c);
        }
    }

    // Getters / Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Option> getOptions() { return options; }
    public List<Criterion> getCriteria() { return criteria; }
}
