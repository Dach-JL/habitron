package com.habitron.engine.model;

/**
 * A single evaluation criterion with a name and weight.
 * Example: "Productivity" with weight 0.4
 * 
 * Weights across all criteria in a session MUST sum to 1.0.
 */
public class Criterion {
    private String name;
    private double weight; // 0.0 to 1.0, all weights must sum to 1.0

    public Criterion() {}

    public Criterion(String name, double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Criterion weight must be between 0.0 and 1.0");
        }
        this.name = name;
        this.weight = weight;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Criterion weight must be between 0.0 and 1.0");
        }
        this.weight = weight;
    }
}
