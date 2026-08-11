package com.axiom.client.module;

/**
 * Categories used to group modules inside the ClickGUI.
 */
public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    VISUAL("Visual"),
    MISC("Misc");

    public final String display;

    Category(String display) {
        this.display = display;
    }
}
