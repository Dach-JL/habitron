package com.habitron.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The core decision container. Holds up to 5 options and 5 criteria.
 * Criteria weights MUST sum to 1.0 (validated on creation).
 * No missing values are allowed — all scores must be populated.
 *
 * The scores matrix: scores[optionIndex][criterionIndex]
 * represents how each option rates on each criterion (0-10 scale).
 */
public class DecisionSession {

    public static final int MAX_OPTIONS = 5;
    public static final int MAX_CRITERIA = 5;
    private static final double WEIGHT_SUM_TOLERANCE = 0.01;

    private String name;
    private List<Option> options;
    private List<Criterion> criteria;
    private double[][] scores;  // [option][criterion] -> 0.0 to 10.0
    private List<Goal> alignedGoals;
    private int recommendedIndex; // index of the engine's recommendation
    private int chosenIndex;      // index of what the user actually chose
    private long timestamp;       // epoch millis of decision time
    private boolean completed;    // has user confirmed their choice?

    public DecisionSession() {
        this.options = new ArrayList<>();
        this.criteria = new ArrayList<>();
        this.alignedGoals = new ArrayList<>();
        this.recommendedIndex = -1;
        this.chosenIndex = -1;
        this.completed = false;
    }

    public DecisionSession(String name) {
        this();
        this.name = name;
    }

    // === Strict Constraint Enforcement ===

    /** Add an option. Throws if already at MAX_OPTIONS. */
    public void addOption(Option option) {
        if (options.size() >= MAX_OPTIONS) {
            throw new IllegalStateException(
                "Maximum " + MAX_OPTIONS + " options allowed. Stop overthinking.");
        }
        options.add(option);
    }

    /** Add a criterion. Throws if already at MAX_CRITERIA. */
    public void addCriterion(Criterion criterion) {
        if (criteria.size() >= MAX_CRITERIA) {
            throw new IllegalStateException(
                "Maximum " + MAX_CRITERIA + " criteria allowed. Keep it simple.");
        }
        criteria.add(criterion);
    }

    /** Add an aligned goal for this decision. */
    public void addGoal(Goal goal) {
        alignedGoals.add(goal);
    }

    /**
     * Validate the session before scoring.
     * - Must have at least 2 options
     * - Must have at least 1 criterion
     * - Criteria weights must sum to 1.0 (±tolerance)
     * - All scores must be populated (no nulls, no missing values)
     */
    public void validate() {
        if (options.size() < 2) {
            throw new IllegalStateException(
                "At least 2 options required. You need a real choice.");
        }
        if (criteria.isEmpty()) {
            throw new IllegalStateException(
                "At least 1 criterion required. What are you even evaluating?");
        }

        // Validate weight sum
        double weightSum = 0.0;
        for (Criterion c : criteria) {
            weightSum += c.getWeight();
        }
        if (Math.abs(weightSum - 1.0) > WEIGHT_SUM_TOLERANCE) {
            throw new IllegalStateException(
                "Criteria weights must sum to 1.0. Current sum: " + weightSum);
        }

        // Validate scores matrix
        if (scores == null) {
            throw new IllegalStateException(
                "Scores matrix not set. No missing values allowed.");
        }
        if (scores.length != options.size()) {
            throw new IllegalStateException(
                "Scores matrix row count doesn't match option count.");
        }
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == null || scores[i].length != criteria.size()) {
                throw new IllegalStateException(
                    "Scores for option '" + options.get(i).getName()
                    + "' are incomplete. No missing values allowed.");
            }
            for (int j = 0; j < scores[i].length; j++) {
                if (scores[i][j] < 0.0 || scores[i][j] > 10.0) {
                    throw new IllegalStateException(
                        "Score for '" + options.get(i).getName()
                        + "' on '" + criteria.get(j).getName()
                        + "' must be between 0 and 10.");
                }
            }
        }
    }

    /** Did the user follow the engine's recommendation? */
    public boolean followedRecommendation() {
        return completed && recommendedIndex == chosenIndex;
    }

    // === Getters & Setters ===

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Option> getOptions() { return options; }
    public List<Criterion> getCriteria() { return criteria; }
    public List<Goal> getAlignedGoals() { return alignedGoals; }

    public double[][] getScores() { return scores; }
    public void setScores(double[][] scores) { this.scores = scores; }

    public int getRecommendedIndex() { return recommendedIndex; }
    public void setRecommendedIndex(int recommendedIndex) { this.recommendedIndex = recommendedIndex; }

    public int getChosenIndex() { return chosenIndex; }
    public void setChosenIndex(int chosenIndex) { this.chosenIndex = chosenIndex; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public int getOptionCount() { return options.size(); }
    public int getCriteriaCount() { return criteria.size(); }
}
