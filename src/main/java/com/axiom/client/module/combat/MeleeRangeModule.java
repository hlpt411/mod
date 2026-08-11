package com.axiom.client.module.combat;

import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.NumberSetting;

/**
 * Expands the player's entity interaction range to simulate latency or test hitboxes.
 * The mixin reads this module and replaces vanilla reach when it is active.
 */
public class MeleeRangeModule extends Module {
    private final NumberSetting range = addSetting(new NumberSetting("Range", 3.5, 3.0, 10.0, 0.1));

    public MeleeRangeModule() {
        super("MeleeRange", "Adjusts attack interaction distance", Category.COMBAT);
        setKey(82); // R
    }

    public double getRange() {
        return range.getValue();
    }
}
