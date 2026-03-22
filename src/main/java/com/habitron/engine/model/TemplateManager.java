package com.habitron.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages decision templates.
 * Provides a set of default templates and allows user-defined ones.
 */
public class TemplateManager {
    private List<DecisionTemplate> templates;

    public TemplateManager() {
        this.templates = new ArrayList<>();
        loadDefaults();
    }

    private void loadDefaults() {
        // Morning Routine
        DecisionTemplate morning = new DecisionTemplate("morning", "Morning Routine");
        morning.addOption(new Option("Deep Work", false, true));
        morning.addOption(new Option("Light Work", true, false));
        morning.addOption(new Option("Exercise", false, true));
        morning.addCriterion(new Criterion("Productivity", 0.6));
        morning.addCriterion(new Criterion("Health", 0.4));
        templates.add(morning);

        // Evening Wind Down
        DecisionTemplate evening = new DecisionTemplate("evening", "Evening Wind Down");
        evening.addOption(new Option("Read Book", true, true));
        evening.addOption(new Option("Social Media", true, false));
        evening.addOption(new Option("Meditate", true, true));
        evening.addCriterion(new Criterion("Relaxation", 0.7));
        evening.addCriterion(new Criterion("Growth", 0.3));
        templates.add(evening);

        // Daily Task
        DecisionTemplate task = new DecisionTemplate("daily", "High Stakes Task");
        task.addOption(new Option("Focus Now", false, true));
        task.addOption(new Option("Delay 1hr", true, false));
        task.addCriterion(new Criterion("Deadline Pressure", 0.5));
        task.addCriterion(new Criterion("Energy Level", 0.5));
        templates.add(task);
    }

    public List<DecisionTemplate> getTemplates() {
        return templates;
    }

    public DecisionTemplate getTemplateById(String id) {
        for (DecisionTemplate t : templates) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }
}
