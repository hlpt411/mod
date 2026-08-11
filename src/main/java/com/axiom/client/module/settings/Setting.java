package com.axiom.client.module.settings;

/**
 * Base class for all module settings.
 * Keeps a name and a typed value that the config system can save/load.
 */
public abstract class Setting<T> {
    protected final String name;
    protected T value;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns a config-safe representation of the value.
     */
    public abstract Object toConfigValue();

    /**
     * Restores the value from a config object.
     */
    public abstract void fromConfigValue(Object object);
}
