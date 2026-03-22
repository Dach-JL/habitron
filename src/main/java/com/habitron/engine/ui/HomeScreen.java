package com.habitron.engine.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Home screen — the dashboard showing streak status, last decision summary,
 * and the call-to-action to start a new decision.
 */
public class HomeScreen {

    private final HTMLDocument document;

    public HomeScreen(HTMLDocument document) {
        this.document = document;
    }

    /** Populate the home screen with initial content */
    public void render(int streak, int totalDecisions, double comfortRatio) {
        HTMLElement container = document.getElementById("screen-home");
        if (container == null) return;

        String streakClass = streak > 0 ? "stat-good" : "stat-warning";
        String ratioClass = comfortRatio < 0.5 ? "stat-good" : "stat-bad";
        int disciplinePercent = (int) ((1.0 - comfortRatio) * 100);

        StringBuilder html = new StringBuilder();
        html.append("<div class='dashboard'>");

        // Streak card
        html.append("<div class='stat-card'>");
        html.append("<div class='stat-icon'>🔥</div>");
        html.append("<div class='stat-value ").append(streakClass).append("'>").append(streak).append("</div>");
        html.append("<div class='stat-label'>Day Streak</div>");
        html.append("</div>");

        // Total decisions card
        html.append("<div class='stat-card'>");
        html.append("<div class='stat-icon'>⚡</div>");
        html.append("<div class='stat-value'>").append(totalDecisions).append("</div>");
        html.append("<div class='stat-label'>Total Decisions</div>");
        html.append("</div>");

        // Discipline ratio card
        html.append("<div class='stat-card'>");
        html.append("<div class='stat-icon'>💪</div>");
        html.append("<div class='stat-value ").append(ratioClass).append("'>").append(disciplinePercent).append("%</div>");
        html.append("<div class='stat-label'>Discipline Rate</div>");
        html.append("</div>");

        html.append("</div>"); // .dashboard

        // CTA
        html.append("<button id='btn-new-decision' class='btn-primary'>Make a Decision</button>");
        html.append("<p class='subtext'>Stop overthinking. Decide in under 20 seconds.</p>");

        container.setInnerHTML(html.toString());
    }
}
