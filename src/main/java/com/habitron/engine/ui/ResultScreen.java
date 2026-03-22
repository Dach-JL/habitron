package com.habitron.engine.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Result screen — displays the engine's recommendation with confidence metrics.
 * Placeholder for now, will be fully built in Phase 9.
 */
public class ResultScreen {

    private final HTMLDocument document;

    public ResultScreen(HTMLDocument document) {
        this.document = document;
    }

    /** Render the result placeholder */
    public void render() {
        HTMLElement container = document.getElementById("screen-result");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>Recommendation</h2>");
        html.append("<p class='screen-desc'>The engine has spoken. Follow or face failure logging.</p>");
        html.append("</div>");

        html.append("<div class='placeholder-content'>");
        html.append("<div class='placeholder-icon'>⚖️</div>");
        html.append("<p>Scoring engine output coming in Phase 7-9</p>");
        html.append("<p class='subtext'>Base Score • Discipline Boost • Comfort Penalty</p>");
        html.append("</div>");

        container.setInnerHTML(html.toString());
    }
}
