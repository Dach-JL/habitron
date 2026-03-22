package com.habitron.engine.model;

/**
 * Represents a personal goal the user is working towards.
 * Used for Goal Alignment scoring adjustments.
 * Example: "Get healthier", "Save money"
 */
public class Goal {
    private String name;
    private String description;
    private double priority; // 0.0 to 1.0, how important this goal is

    public Goal() {}

    public Goal(String name, String description, double priority) {
        if (priority < 0.0 || priority > 1.0) {
            throw new IllegalArgumentException("Goal priority must be between 0.0 and 1.0");
        }
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPriority() { return priority; }
    public void setPriority(double priority) {
        if (priority < 0.0 || priority > 1.0) {
            throw new IllegalArgumentException("Goal priority must be between 0.0 and 1.0");
        }
        this.priority = priority;
    }
}
