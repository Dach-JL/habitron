package com.habitron.engine.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Lightweight SPA router. Manages view switching by showing/hiding
 * screen containers. No hash-based routing needed — pure memory-based.
 */
public class Router {

    public static final String SCREEN_HOME = "screen-home";
    public static final String SCREEN_DECISION = "screen-decision";
    public static final String SCREEN_RESULT = "screen-result";
    public static final String SCREEN_INSIGHTS = "screen-insights";

    private static final String[] ALL_SCREENS = {
        SCREEN_HOME, SCREEN_DECISION, SCREEN_RESULT, SCREEN_INSIGHTS
    };

    private final HTMLDocument document;
    private String activeScreen;

    public Router(HTMLDocument document) {
        this.document = document;
        this.activeScreen = SCREEN_HOME;
    }

    /** Navigate to a screen by ID. Hides all others. */
    public void navigateTo(String screenId) {
        for (String id : ALL_SCREENS) {
            HTMLElement el = document.getElementById(id);
            if (el != null) {
                if (id.equals(screenId)) {
                    el.setAttribute("class", "screen active");
                } else {
                    el.setAttribute("class", "screen");
                }
            }
        }
        activeScreen = screenId;
        updateNav(screenId);
    }

    /** Highlight the active nav button */
    private void updateNav(String screenId) {
        String[] navIds = {"nav-home", "nav-decision", "nav-result", "nav-insights"};
        String[] screenIds = {SCREEN_HOME, SCREEN_DECISION, SCREEN_RESULT, SCREEN_INSIGHTS};

        for (int i = 0; i < navIds.length; i++) {
            HTMLElement btn = document.getElementById(navIds[i]);
            if (btn != null) {
                if (screenIds[i].equals(screenId)) {
                    btn.setAttribute("class", "nav-btn active");
                } else {
                    btn.setAttribute("class", "nav-btn");
                }
            }
        }
    }

    public String getActiveScreen() {
        return activeScreen;
    }
}
