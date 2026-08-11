package com.axiom.client.module.settings;

/**
 * Floating-point setting clamped between a min and max value.
 * Used for ranges, delays, knockback percentages, etc.
 */
public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.step = step;
        clamp();
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public void setValueDouble(double v) {
        this.value = v;
        clamp();
    }

    private void clamp() {
        if (value < min) value = min;
        if (value > max) value = max;
        if (step > 0) {
            value = Math.round((value - min) / step) * step + min;
        }
    }

    @Override
    public void setValue(Double value) {
        super.setValue(value);
        clamp();
    }

    @Override
    public Object toConfigValue() {
        return value;
    }

    @Override
    public void fromConfigValue(Object object) {
        if (object instanceof Number n) setValue(n.doubleValue());
    }
}
