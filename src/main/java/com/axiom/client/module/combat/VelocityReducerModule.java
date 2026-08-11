package com.axiom.client.module.combat;

import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.NumberSetting;

/**
 * Scales incoming knockback on the local player.
 * 0% means no knockback, 100% is vanilla behaviour.
 */
public class VelocityReducerModule extends Module {
    private final NumberSetting percent = addSetting(new NumberSetting("Percent", 0.0, 0.0, 100.0, 5.0));

    public VelocityReducerModule() {
        super("VelocityReducer", "Scales knockback received", Category.COMBAT);
        setKey(86); // V
    }

    public double getPercent() {
        return percent.getValue();
    }
}
