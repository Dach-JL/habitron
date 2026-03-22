package com.habitron.engine;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class Client {
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement status = document.getElementById("status");
        
        if (status != null) {
            status.setInnerHTML("TeaVM Engine Initialized. Ready for Phase 2.");
            status.getStyle().setProperty("color", "var(--color-discipline)");
        }
    }
}
