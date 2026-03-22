package com.habitron.engine.ui;

import com.habitron.engine.model.DecisionSession;
import com.habitron.engine.model.Option;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Result screen — displays the engine's recommendation with confidence metrics.
 */
public class ResultScreen {

    private final HTMLDocument document;
    private Router router;

    public ResultScreen(HTMLDocument document) {
        this.document = document;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    /** Render the decision result */
    public void render(DecisionSession session, double[] baseScores) {
        HTMLElement container = document.getElementById("screen-result");
        if (container == null) return;
        if (session == null || baseScores == null) {
            renderPlaceholder();
            return;
        }

        int recIdx = session.getRecommendedIndex();
        Option recommended = session.getOptions().get(recIdx);

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>The Recommendation</h2>");
        html.append("<p class='screen-desc'>The engine has evaluated all paths. This is your move.</p>");
        html.append("</div>");

        // Recommendation Card
        html.append("<div class='recommendation-card ").append(recommended.isDiscipline() ? "discipline" : "comfort").append("'>");
        html.append("<div class='rec-label'>ENGINE ADVICE</div>");
        html.append("<div class='rec-name'>").append(recommended.getName()).append("</div>");
        html.append("<div class='rec-score'>Score: ").append(String.format("%.2f", baseScores[recIdx])).append(" / 10</div>");
        html.append("</div>");

        // Comparison List
        html.append("<div class='result-list'>");
        
        // Sort indices by score descending
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < session.getOptions().size(); i++) indices.add(i);
        indices.sort((a, b) -> Double.compare(baseScores[b], baseScores[a]));

        for (int i : indices) {
            Option o = session.getOptions().get(i);
            boolean isRec = (i == recIdx);
            html.append("<div class='result-item ").append(isRec ? "active" : "").append("'>");
            html.append("<div class='result-info'>");
            html.append("<div class='result-name'>").append(o.getName()).append("</div>");
            html.append("<div class='result-tags'>");
            if (o.isDiscipline()) html.append("<span class='tag disc'>Hard</span>");
            if (o.isComfort()) html.append("<span class='tag comfort'>Easy</span>");
            html.append("</div>");
            html.append("</div>");
            html.append("<div class='result-value'>").append(String.format("%.1f", baseScores[i])).append("</div>");
            html.append("</div>");
        }
        html.append("</div>");

        html.append("<div class='actions mt-20'>");
        html.append("<button id='btn-confirm-choice' class='btn-primary'>Confirm Action Taken</button>");
        html.append("<button id='btn-back-home' class='btn-secondary mt-10'>Back to Dashboard</button>");
        html.append("</div>");

        container.setInnerHTML(html.toString());

        // Wire events
        document.getElementById("btn-back-home").addEventListener("click", evt -> router.navigateTo(Router.SCREEN_HOME));
        document.getElementById("btn-confirm-choice").addEventListener("click", evt -> {
            // Logic for Phase 10: Feedback Loop
            router.navigateTo(Router.SCREEN_HOME);
        });
    }

    private void renderPlaceholder() {
        HTMLElement container = document.getElementById("screen-result");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>Recommendation</h2>");
        html.append("<p class='screen-desc'>No active decision session found.</p>");
        html.append("</div>");

        html.append("<div class='placeholder-content'>");
        html.append("<div class='placeholder-icon'>⚖️</div>");
        html.append("<p>Start a new decision to see the engine's advice.</p>");
        html.append("</div>");

        container.setInnerHTML(html.toString());
    }

    // Default render for initialization
    public void render() {
        renderPlaceholder();
    }
}
