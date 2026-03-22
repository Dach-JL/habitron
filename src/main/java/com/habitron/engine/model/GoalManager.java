package com.habitron.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the user's personal goals. Goals are mapped to the
 * BehaviorProfile and used for Goal Alignment scoring.
 * Max 5 goals allowed to keep focus sharp.
 */
public class GoalManager {

    public static final int MAX_GOALS = 5;
    private List<Goal> goals;

    public GoalManager() {
        this.goals = new ArrayList<>();
    }

    /** Add a goal. Throws if already at MAX_GOALS. */
    public void addGoal(Goal goal) {
        if (goals.size() >= MAX_GOALS) {
            throw new IllegalStateException(
                "Maximum " + MAX_GOALS + " goals allowed. Stay focused.");
        }
        goals.add(goal);
    }

    /** Remove a goal by index */
    public void removeGoal(int index) {
        if (index >= 0 && index < goals.size()) {
            goals.remove(index);
        }
    }

    /** Get all goals */
    public List<Goal> getGoals() {
        return goals;
    }

    /** Get goal count */
    public int getCount() {
        return goals.size();
    }

    /** Check if a goal with the given name already exists */
    public boolean hasGoal(String name) {
        for (Goal g : goals) {
            if (g.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Calculate total goal alignment score for a given option name */
    public double getAlignmentScore(String optionName, String optionDescription) {
        if (goals.isEmpty()) return 0.0;
        // Alignment is calculated in the scoring engine (Phase 8)
        // For now, return sum of priorities as a baseline
        double total = 0.0;
        for (Goal g : goals) {
            total += g.getPriority();
        }
        return total / goals.size();
    }
}
