package com.habitron.engine.model;

/**
 * Represents a single option in a decision.
 * Example: "Study" or "Watch TV"
 */
public class Option {
    private String name;
    private boolean isComfort;   // true = easy/comfortable choice
    private boolean isDiscipline; // true = hard/productive choice

    public Option() {}

    public Option(String name, boolean isComfort, boolean isDiscipline) {
        this.name = name;
        this.isComfort = isComfort;
        this.isDiscipline = isDiscipline;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isComfort() { return isComfort; }
    public void setComfort(boolean comfort) { isComfort = comfort; }

    public boolean isDiscipline() { return isDiscipline; }
    public void setDiscipline(boolean discipline) { isDiscipline = discipline; }
}
