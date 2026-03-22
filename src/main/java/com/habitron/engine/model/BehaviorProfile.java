package com.habitron.engine.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks long-term behavioral patterns.
 * Records how often the user picks comfort vs discipline,
 * maintains streaks, and logs failures.
 */
public class BehaviorProfile {
    private Map<String, Integer> optionFrequency; // option name -> times chosen
    private int disciplineStreak;     // consecutive disciplined choices
    private int comfortCount;         // total comfort choices made
    private int disciplineCount;      // total discipline choices made
    private int totalDecisions;       // total decisions made
    private int failureCount;         // times user ignored recommendation

    public BehaviorProfile() {
        this.optionFrequency = new HashMap<>();
        this.disciplineStreak = 0;
        this.comfortCount = 0;
        this.disciplineCount = 0;
        this.totalDecisions = 0;
        this.failureCount = 0;
    }

    /** Record that the user chose an option */
    public void recordChoice(Option chosen, boolean followedRecommendation) {
        String key = chosen.getName();
        int current = optionFrequency.containsKey(key) ? optionFrequency.get(key) : 0;
        optionFrequency.put(key, current + 1);
        totalDecisions++;

        if (chosen.isDiscipline()) {
            disciplineCount++;
            disciplineStreak++;
        } else if (chosen.isComfort()) {
            comfortCount++;
            disciplineStreak = 0; // streak broken
        }

        if (!followedRecommendation) {
            failureCount++;
        }
    }

    /** Get comfort-to-discipline ratio (0.0 = all discipline, 1.0 = all comfort) */
    public double getComfortRatio() {
        if (totalDecisions == 0) return 0.0;
        return (double) comfortCount / totalDecisions;
    }

    /** Get frequency of a specific option being chosen */
    public int getOptionFrequency(String optionName) {
        return optionFrequency.containsKey(optionName) ? optionFrequency.get(optionName) : 0;
    }

    // Getters
    public Map<String, Integer> getOptionFrequencyMap() { return optionFrequency; }
    public int getDisciplineStreak() { return disciplineStreak; }
    public int getComfortCount() { return comfortCount; }
    public int getDisciplineCount() { return disciplineCount; }
    public int getTotalDecisions() { return totalDecisions; }
    public int getFailureCount() { return failureCount; }
}
