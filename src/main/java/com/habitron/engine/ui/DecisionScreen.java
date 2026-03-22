package com.habitron.engine.ui;

import com.habitron.engine.model.*;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Decision screen — where the user inputs options, criteria, and scores.
 */
public class DecisionScreen {

    private final HTMLDocument document;
    private final TemplateManager templateManager;
    private final ScoringEngine scoringEngine;
    private DecisionSession currentSession;
    private Router router;
    private ResultScreen resultScreen;

    public DecisionScreen(HTMLDocument document, TemplateManager templateManager) {
        this.document = document;
        this.templateManager = templateManager;
        this.scoringEngine = new ScoringEngine();
    }

    public void setRouter(Router router, ResultScreen resultScreen) {
        this.router = router;
        this.resultScreen = resultScreen;
    }

    /** Render the template selection grid */
    public void render() {
        HTMLElement container = document.getElementById("screen-decision");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<h2>New Decision</h2>");
        html.append("<p class='screen-desc'>Select a template to start or create a blank one.</p>");
        html.append("</div>");

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
        wireTemplateEvents();
    }

    private String getIconForTemplate(String id) {
        switch (id) {
            case "morning": return "☀️";
            case "evening": return "🌙";
            case "daily": return "🔥";
            default: return "📋";
        }
    }

    private void wireTemplateEvents() {
        HTMLElement blank = document.getElementById("template-blank");
        if (blank != null) {
            blank.addEventListener("click", evt -> startDecision(null));
        }

        for (DecisionTemplate t : templateManager.getTemplates()) {
            HTMLElement tel = document.getElementById("template-" + t.getId());
            if (tel != null) {
                tel.addEventListener("click", evt -> startDecision(t));
            }
        }
    }

    private void startDecision(DecisionTemplate template) {
        currentSession = new DecisionSession((template == null) ? "New Decision" : template.getName());
        
        if (template != null) {
            for (Option o : template.getOptions()) currentSession.addOption(new Option(o.getName(), o.isComfort(), o.isDiscipline()));
            for (Criterion c : template.getCriteria()) currentSession.addCriterion(new Criterion(c.getName(), c.getWeight()));
        } else {
            currentSession.addOption(new Option("Option 1", true, false));
            currentSession.addOption(new Option("Option 2", false, true));
            currentSession.addCriterion(new Criterion("Criterion 1", 1.0));
        }

        renderForm();
    }

    private void renderForm() {
        HTMLElement container = document.getElementById("screen-decision");
        if (container == null) return;

        StringBuilder html = new StringBuilder();
        html.append("<div class='screen-header'>");
        html.append("<button class='btn-back' id='btn-template-back'>← Templates</button>");
        html.append("<h2>").append(currentSession.getName()).append("</h2>");
        html.append("</div>");

        // Options
        html.append("<section class='form-section'>");
        html.append("<div class='section-header'>");
        html.append("<h3>Options (").append(currentSession.getOptionCount()).append("/5)</h3>");
        if (currentSession.getOptionCount() < 5) {
            html.append("<button id='btn-add-option' class='btn-add'>+ Add</button>");
        }
        html.append("</div>");
        html.append("<div class='input-list' id='list-options'>");
        for (int i = 0; i < currentSession.getOptions().size(); i++) {
            Option o = currentSession.getOptions().get(i);
            html.append("<div class='input-row'>");
            html.append("<input type='text' class='input-field' id='opt-name-").append(i).append("' value='").append(o.getName()).append("' placeholder='Option name'>");
            html.append("<label class='toggle-label discipline'><input type='checkbox' id='opt-disc-").append(i).append("' ").append(o.isDiscipline()?"checked":"").append("> Hard</label>");
            if (currentSession.getOptionCount() > 2) {
                html.append("<button class='btn-remove' id='opt-remove-").append(i).append("'>✕</button>");
            }
            html.append("</div>");
        }
        html.append("</div>");
        html.append("</section>");

        // Criteria
        html.append("<section class='form-section'>");
        html.append("<div class='section-header'>");
        html.append("<h3>Criteria (").append(currentSession.getCriteriaCount()).append("/5)</h3>");
        if (currentSession.getCriteriaCount() < 5) {
            html.append("<button id='btn-add-criterion' class='btn-add'>+ Add</button>");
        }
        html.append("</div>");
        html.append("<div class='input-list' id='list-criteria'>");
        for (int i = 0; i < currentSession.getCriteria().size(); i++) {
            Criterion c = currentSession.getCriteria().get(i);
            html.append("<div class='input-row'>");
            html.append("<input type='text' class='input-field' id='crit-name-").append(i).append("' value='").append(c.getName()).append("' placeholder='Criterion name'>");
            html.append("<input type='number' step='0.1' min='0' max='1' class='input-field weight' id='crit-weight-").append(i).append("' value='").append(c.getWeight()).append("'>");
            if (currentSession.getCriteriaCount() > 1) {
                html.append("<button class='btn-remove' id='crit-remove-").append(i).append("'>✕</button>");
            }
            html.append("</div>");
        }
        html.append("</div>");
        html.append("</section>");

        // Score Matrix
        html.append("<section class='form-section'>");
        html.append("<h3>Scores (0-10)</h3>");
        html.append("<div class='matrix-container'>");
        html.append("<table class='score-matrix'>");
        html.append("<tr><th></th>");
        for (Criterion c : currentSession.getCriteria()) {
            html.append("<th>").append(c.getName().isEmpty() ? "?" : c.getName()).append("</th>");
        }
        html.append("</tr>");

        double[][] existingScores = currentSession.getScores();
        for (int i = 0; i < currentSession.getOptions().size(); i++) {
            Option o = currentSession.getOptions().get(i);
            html.append("<tr>");
            html.append("<td class='opt-header-cell'>").append(o.getName().isEmpty() ? "?" : o.getName()).append("</td>");
            for (int j = 0; j < currentSession.getCriteria().size(); j++) {
                double val = (existingScores != null && i < existingScores.length && j < existingScores[i].length) ? existingScores[i][j] : 5.0;
                html.append("<td><input type='number' min='0' max='10' class='input-field matrix-input' id='score-").append(i).append("-").append(j).append("' value='").append(val).append("'></td>");
            }
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</div>");
        html.append("</section>");

        html.append("<button id='btn-calculate' class='btn-primary mt-20'>Score Decision</button>");
        html.append("<div id='form-error' class='error-text'></div>");

        container.setInnerHTML(html.toString());
        wireFormEvents();
    }

    private void wireFormEvents() {
        document.getElementById("btn-template-back").addEventListener("click", evt -> render());

        HTMLElement addOpt = document.getElementById("btn-add-option");
        if (addOpt != null) addOpt.addEventListener("click", evt -> { saveState(); currentSession.addOption(new Option("New Option", false, false)); renderForm(); });

        HTMLElement addCrit = document.getElementById("btn-add-criterion");
        if (addCrit != null) addCrit.addEventListener("click", evt -> { saveState(); currentSession.addCriterion(new Criterion("New Criterion", 0.0)); renderForm(); });

        for (int i = 0; i < currentSession.getOptionCount(); i++) {
            final int idx = i;
            HTMLElement btn = document.getElementById("opt-remove-" + i);
            if (btn != null) btn.addEventListener("click", evt -> { saveState(); currentSession.getOptions().remove(idx); renderForm(); });
        }
        for (int i = 0; i < currentSession.getCriteriaCount(); i++) {
            final int idx = i;
            HTMLElement btn = document.getElementById("crit-remove-" + i);
            if (btn != null) btn.addEventListener("click", evt -> { saveState(); currentSession.getCriteria().remove(idx); renderForm(); });
        }

        document.getElementById("btn-calculate").addEventListener("click", evt -> {
            saveState();
            try {
                currentSession.validate();
                
                // --- Phase 7 Logic ---
                double[] baseScores = scoringEngine.calculateBaseScores(currentSession);
                int recommendedIdx = scoringEngine.getRecommendedIndex(baseScores);
                currentSession.setRecommendedIndex(recommendedIdx);
                
                // Show results
                resultScreen.render(currentSession, baseScores);
                router.navigateTo(Router.SCREEN_RESULT);
                
            } catch (Exception e) {
                HTMLElement err = document.getElementById("form-error");
                err.setInnerHTML(e.getMessage());
                err.getStyle().setProperty("color", "var(--color-failure)");
            }
        });
    }

    private void saveState() {
        for (int i = 0; i < currentSession.getOptionCount(); i++) {
            HTMLInputElement nameInp = (HTMLInputElement) document.getElementById("opt-name-" + i);
            HTMLInputElement discInp = (HTMLInputElement) document.getElementById("opt-disc-" + i);
            if (nameInp != null) currentSession.getOptions().get(i).setName(nameInp.getValue());
            if (discInp != null) currentSession.getOptions().get(i).setDiscipline(discInp.isChecked());
        }
        for (int i = 0; i < currentSession.getCriteriaCount(); i++) {
            HTMLInputElement nameInp = (HTMLInputElement) document.getElementById("crit-name-" + i);
            HTMLInputElement weightInp = (HTMLInputElement) document.getElementById("crit-weight-" + i);
            if (nameInp != null) currentSession.getCriteria().get(i).setName(nameInp.getValue());
            if (weightInp != null) { try { currentSession.getCriteria().get(i).setWeight(Double.parseDouble(weightInp.getValue())); } catch (Exception e) {} }
        }
        double[][] scores = new double[currentSession.getOptionCount()][currentSession.getCriteriaCount()];
        for (int i = 0; i < currentSession.getOptionCount(); i++) {
            for (int j = 0; j < currentSession.getCriteriaCount(); j++) {
                HTMLInputElement scoreInp = (HTMLInputElement) document.getElementById("score-" + i + "-" + j);
                if (scoreInp != null) { try { scores[i][j] = Double.parseDouble(scoreInp.getValue()); } catch (Exception e) { scores[i][j] = 0.0; } }
            }
        }
        currentSession.setScores(scores);
    }
}
