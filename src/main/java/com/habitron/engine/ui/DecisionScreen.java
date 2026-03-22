package com.habitron.engine.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Decision screen — where the user inputs options, criteria, and scores.
 * Placeholder for now, will be fully built in Phases 5-6.
 */
public class DecisionScreen {

    private final HTMLDocument document;

    public DecisionScreen(HTMLDocument document) {
        this.document = document;
    }

    /** Render the decision input placeholder */
    public void render() {
        HTMLElement container = document.getElementById("screen-decision");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>New Decision</h2>");
        html.append("<p class='screen-desc'>Define your options and criteria. Max 5 each. No excuses.</p>");
        html.append("</div>");

        html.append("<div class='placeholder-content'>");
        html.append("<div class='placeholder-icon'>🎯</div>");
        html.append("<p>Decision input form coming in Phase 5-6</p>");
        html.append("<p class='subtext'>Templates • Options • Criteria • Scores</p>");
        html.append("</div>");

        container.setInnerHTML(html.toString());
    }
}
