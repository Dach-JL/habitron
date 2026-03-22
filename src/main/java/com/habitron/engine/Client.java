package com.habitron.engine;

import com.habitron.engine.model.*;
import com.habitron.engine.ui.*;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Event;

public class Client {

    private static Router router;
    private static HomeScreen homeScreen;
    private static DecisionScreen decisionScreen;
    private static ResultScreen resultScreen;
    private static InsightsScreen insightsScreen;
    private static GoalScreen goalScreen;
    private static BehaviorProfile profile;
    private static GoalManager goalManager;
    private static TemplateManager templateManager;

    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        profile = new BehaviorProfile();
        templateManager = new TemplateManager();

        // Initialize screens
        router = new Router(document);
        homeScreen = new HomeScreen(document);
        decisionScreen = new DecisionScreen(document, templateManager);
        resultScreen = new ResultScreen(document);
        insightsScreen = new InsightsScreen(document);
        goalManager = new GoalManager();
        goalScreen = new GoalScreen(document, goalManager);

        // Render all screens
        homeScreen.render(
            profile.getDisciplineStreak(),
            profile.getTotalDecisions(),
            profile.getComfortRatio()
        );
        decisionScreen.render();
        resultScreen.render();
        insightsScreen.render();
        goalScreen.render();

        // Wire up navigation
        wireNav(document, "nav-home", Router.SCREEN_HOME);
        wireNav(document, "nav-decision", Router.SCREEN_DECISION);
        wireNav(document, "nav-result", Router.SCREEN_RESULT);
        wireNav(document, "nav-insights", Router.SCREEN_INSIGHTS);
        wireNav(document, "nav-goals", Router.SCREEN_GOALS);

        // Wire "Make a Decision" CTA button
        HTMLElement ctaBtn = document.getElementById("btn-new-decision");
        if (ctaBtn != null) {
            ctaBtn.addEventListener("click", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    router.navigateTo(Router.SCREEN_DECISION);
                }
            });
        }

        // Start on home screen
        router.navigateTo(Router.SCREEN_HOME);
    }

    private static void wireNav(HTMLDocument document, String navId, String screenId) {
        HTMLElement btn = document.getElementById(navId);
        if (btn != null) {
            btn.addEventListener("click", new EventListener<Event>() {
                @Override
                public void handleEvent(Event evt) {
                    router.navigateTo(screenId);
                }
            });
        }
    }
}
