package com.habitron.engine.ui;

import com.habitron.engine.model.Goal;
import com.habitron.engine.model.GoalManager;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Event;

/**
 * Goal management screen. Users can add, view, and remove personal goals.
 * Each goal has a name, description, and priority weight (0.0–1.0).
 */
public class GoalScreen {

    private final HTMLDocument document;
    private final GoalManager goalManager;

    public GoalScreen(HTMLDocument document, GoalManager goalManager) {
        this.document = document;
        this.goalManager = goalManager;
    }

    /** Render the full goal management screen */
    public void render() {
        HTMLElement container = document.getElementById("screen-goals");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>Your Goals</h2>");
        html.append("<p class='screen-desc'>Define what matters. Max 5 goals. The engine aligns decisions to these.</p>");
        html.append("</div>");

        // Add goal form
        html.append("<div class='goal-form'>");
        html.append("<input id='goal-name' class='input-field' type='text' placeholder='Goal name (e.g., Get Healthier)' maxlength='40'>");
        html.append("<input id='goal-desc' class='input-field' type='text' placeholder='Brief description' maxlength='80'>");
        html.append("<div class='priority-row'>");
        html.append("<label class='priority-label'>Priority</label>");
        html.append("<input id='goal-priority' class='input-range' type='range' min='0' max='100' value='50'>");
        html.append("<span id='priority-value' class='priority-display'>0.50</span>");
        html.append("</div>");
        html.append("<button id='btn-add-goal' class='btn-primary'>Add Goal</button>");
        html.append("</div>");

        // Goal count
        html.append("<div class='goal-count'>");
        html.append("<span id='goal-counter'>").append(goalManager.getCount()).append("</span> / ").append(GoalManager.MAX_GOALS).append(" goals defined");
        html.append("</div>");

        // Goal list
        html.append("<div id='goal-list' class='goal-list'>");
        renderGoalItems(html);
        html.append("</div>");

        container.setInnerHTML(html.toString());

        // Wire up event listeners after render
        wireEvents();
    }

    /** Render just the goal list items */
    private void renderGoalItems(StringBuilder html) {
        if (goalManager.getCount() == 0) {
            html.append("<div class='empty-state'>");
            html.append("<p>No goals defined yet.</p>");
            html.append("<p class='subtext'>Without goals, you're just drifting.</p>");
            html.append("</div>");
            return;
        }

        for (int i = 0; i < goalManager.getGoals().size(); i++) {
            Goal g = goalManager.getGoals().get(i);
            String priorityColor = g.getPriority() >= 0.7 ? "stat-good" :
                                   g.getPriority() >= 0.4 ? "stat-warning" : "stat-bad";
            int pctValue = (int)(g.getPriority() * 100);

            html.append("<div class='goal-item'>");
            html.append("<div class='goal-info'>");
            html.append("<div class='goal-name'>").append(g.getName()).append("</div>");
            html.append("<div class='goal-description'>").append(g.getDescription()).append("</div>");
            html.append("</div>");
            html.append("<div class='goal-priority ").append(priorityColor).append("'>").append(pctValue).append("%</div>");
            html.append("<button class='btn-remove' id='remove-goal-").append(i).append("'>✕</button>");
            html.append("</div>");
        }
    }

    /** Wire up interactive events */
    private void wireEvents() {
        // Priority slider live update
        HTMLInputElement slider = (HTMLInputElement) document.getElementById("goal-priority");
        HTMLElement display = document.getElementById("priority-value");
        if (slider != null && display != null) {
            slider.addEventListener("input", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    int val = Integer.parseInt(slider.getValue());
                    double priority = val / 100.0;
                    String formatted = formatPriority(priority);
                    display.setInnerHTML(formatted);
                }
            });
        }

        // Add goal button
        HTMLElement addBtn = document.getElementById("btn-add-goal");
        if (addBtn != null) {
            addBtn.addEventListener("click", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    addGoalFromForm();
                }
            });
        }

        // Remove buttons
        for (int i = 0; i < goalManager.getCount(); i++) {
            final int index = i;
            HTMLElement removeBtn = document.getElementById("remove-goal-" + i);
            if (removeBtn != null) {
                removeBtn.addEventListener("click", new EventListener<Event>() {
                    @Override
                    public void handleEvent(Event evt) {
                        goalManager.removeGoal(index);
                        render(); // re-render
                    }
                });
            }
        }
    }

    /** Read form values and add a new goal */
    private void addGoalFromForm() {
        HTMLInputElement nameInput = (HTMLInputElement) document.getElementById("goal-name");
        HTMLInputElement descInput = (HTMLInputElement) document.getElementById("goal-desc");
        HTMLInputElement priorityInput = (HTMLInputElement) document.getElementById("goal-priority");

        if (nameInput == null || descInput == null || priorityInput == null) return;

        String name = nameInput.getValue().trim();
        String desc = descInput.getValue().trim();
        int priorityVal = Integer.parseInt(priorityInput.getValue());
        double priority = priorityVal / 100.0;

        if (name.isEmpty()) return;
        if (goalManager.getCount() >= GoalManager.MAX_GOALS) return;
        if (goalManager.hasGoal(name)) return;

        goalManager.addGoal(new Goal(name, desc, priority));

        // Re-render to show updated list
        render();
    }

    private String formatPriority(double p) {
        int whole = (int) p;
        int frac = (int) ((p - whole) * 100);
        String fracStr = frac < 10 ? "0" + frac : "" + frac;
        return whole + "." + fracStr;
    }
}
