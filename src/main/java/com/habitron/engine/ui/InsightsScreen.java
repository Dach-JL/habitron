package com.habitron.engine.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Insights screen — analytics dashboard showing historical behavior patterns.
 * Placeholder for now, will be fully built in Phase 14.
 */
public class InsightsScreen {

    private final HTMLDocument document;

    public InsightsScreen(HTMLDocument document) {
        this.document = document;
    }

    /** Render the insights placeholder */
    public void render() {
        HTMLElement container = document.getElementById("screen-insights");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>Insights</h2>");
        html.append("<p class='screen-desc'>Your behavioral data. Brutal honesty included.</p>");
        html.append("</div>");

        html.append("<div class='placeholder-content'>");
        html.append("<div class='placeholder-icon'>📊</div>");
        html.append("<p>Analytics dashboard coming in Phase 14</p>");
        html.append("<p class='subtext'>Comfort vs Discipline • Goal Alignment • Trends</p>");
        html.append("</div>");

        container.setInnerHTML(html.toString());
    }
}
