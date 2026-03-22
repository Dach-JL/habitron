package com.habitron.engine;

import com.habitron.engine.model.*;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class Client {
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement status = document.getElementById("status");

        // Smoke-test the data models
        try {
            DecisionSession session = new DecisionSession("Morning Routine");

            session.addOption(new Option("Study", false, true));
            session.addOption(new Option("Watch TV", true, false));
            session.addOption(new Option("Exercise", false, true));

            session.addCriterion(new Criterion("Productivity", 0.5));
            session.addCriterion(new Criterion("Enjoyment", 0.3));
            session.addCriterion(new Criterion("Health", 0.2));

            // Set scores: [option][criterion]
            session.setScores(new double[][] {
                {9.0, 3.0, 5.0},  // Study
                {1.0, 9.0, 2.0},  // Watch TV
                {7.0, 5.0, 9.0}   // Exercise
            });

            session.validate();

            Goal goal = new Goal("Be More Productive", "Focus on deep work", 0.8);
            session.addGoal(goal);

            BehaviorProfile profile = new BehaviorProfile();

            if (status != null) {
                status.setInnerHTML(
                    "Engine Online. " 
                    + session.getOptionCount() + " options, "
                    + session.getCriteriaCount() + " criteria loaded. "
                    + "Constraints validated. Ready for Phase 3."
                );
                status.getStyle().setProperty("color", "var(--color-discipline)");
            }
        } catch (Exception e) {
            if (status != null) {
                status.setInnerHTML("VALIDATION FAILED: " + e.getMessage());
                status.getStyle().setProperty("color", "var(--color-failure)");
            }
        }
    }
}
