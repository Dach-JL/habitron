package com.habitron.engine.ui;

import com.habitron.engine.model.DecisionTemplate;
import com.habitron.engine.model.TemplateManager;
import com.habitron.engine.model.Option;
import com.habitron.engine.model.Criterion;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Event;

/**
 * Decision screen — where the user selects a template or starts a blank decision.
 * Allows quick setup of recurring scenarios.
 */
public class DecisionScreen {

    private final HTMLDocument document;
    private final TemplateManager templateManager;

    public DecisionScreen(HTMLDocument document, TemplateManager templateManager) {
        this.document = document;
        this.templateManager = templateManager;
    }

    /** Render the decision screen with template selection */
    public void render() {
        HTMLElement container = document.getElementById("screen-decision");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>New Decision</h2>");
        html.append("<p class='screen-desc'>Select a template to start or create a blank one.</p>");
        html.append("</div>");

        // Template List
        html.append("<div class='template-grid'>");
        
        // Blank Template
        html.append("<div class='template-card' id='template-blank'>");
        html.append("<div class='template-icon'>📄</div>");
        html.append("<div class='template-name'>Blank Decision</div>");
        html.append("<div class='template-meta'>Start from scratch</div>");
        html.append("</div>");

        for (DecisionTemplate t : templateManager.getTemplates()) {
            html.append("<div class='template-card' id='template-").append(t.getId()).append("'>");
            html.append("<div class='template-icon'>").append(getIconForTemplate(t.getId())).append("</div>");
            html.append("<div class='template-name'>").append(t.getName()).append("</div>");
            html.append("<div class='template-meta'>")
                .append(t.getOptions().size()).append(" options • ")
                .append(t.getCriteria().size()).append(" criteria")
                .append("</div>");
            html.append("</div>");
        }
        
        html.append("</div>");

        container.setInnerHTML(html.toString());

        // Wire up template selection
        wireEvents();
    }

    private String getIconForTemplate(String id) {
        switch (id) {
            case "morning": return "☀️";
            case "evening": return "🌙";
            case "daily": return "🔥";
            default: return "📋";
        }
    }

    private void wireEvents() {
        // Blank template
        HTMLElement blank = document.getElementById("template-blank");
        if (blank != null) {
            blank.addEventListener("click", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    startDecision(null);
                }
            });
        }

        // Named templates
        for (DecisionTemplate t : templateManager.getTemplates()) {
            HTMLElement tel = document.getElementById("template-" + t.getId());
            if (tel != null) {
                tel.addEventListener("click", new EventListener<Event>() {
                    @Override
                    public void handleEvent(Event evt) {
                        startDecision(t);
                    }
                });
            }
        }
    }

    private void startDecision(DecisionTemplate t) {
        // This will be implemented in Phase 6: Input Options & Criteria System
        HTMLElement container = document.getElementById("screen-decision");
        if (container == null) return;

        String title = (t == null) ? "Blank Decision" : t.getName();
        
        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<button class='btn-back' id='btn-template-back'>← Templates</button>");
        html.append("<h2>").append(title).append("</h2>");
        html.append("</div>");

        html.append("<div class='placeholder-content'>");
        html.append("<div class='placeholder-icon'>⚙️</div>");
        html.append("<p>Initial configuration loaded.</p>");
        html.append("<p>Phase 6 will add the interactive form here.</p>");
        
        if (t != null) {
            html.append("<div class='template-summary'>");
            html.append("<strong>Options:</strong><ul>");
            for (Option o : t.getOptions()) html.append("<li>").append(o.getName()).append("</li>");
            html.append("</ul>");
            html.append("<strong>Criteria:</strong><ul>");
            for (Criterion c : t.getCriteria()) html.append("<li>").append(c.getName()).append(" (").append(c.getWeight()).append(")</li>");
            html.append("</ul>");
            html.append("</div>");
        }
        
        html.append("</div>");

        container.setInnerHTML(html.toString());
        
        // Back button
        HTMLElement back = document.getElementById("btn-template-back");
        if (back != null) {
            back.addEventListener("click", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    render(); // show templates again
                }
            });
        }
    }
}
