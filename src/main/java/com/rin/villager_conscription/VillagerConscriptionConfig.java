package com.rin.villager_conscription;

import net.neoforged.neoforge.common.ModConfigSpec;

public class VillagerConscriptionConfig {
    private VillagerConscriptionConfig() {}

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue GUARD_SEARCH_RADIUS;
    public static final ModConfigSpec.DoubleValue VILLAGE_SEARCH_RADIUS;
    public static final ModConfigSpec.DoubleValue VILLAGE_SEARCH_HEIGHT;
    public static final ModConfigSpec.IntValue COOLDOWN_TICKS;

    static {
        BUILDER.push("General");

        GUARD_SEARCH_RADIUS = BUILDER.defineInRange("guardSearchRadius", 16.0, 1.0, 64.0);
        VILLAGE_SEARCH_RADIUS = BUILDER.defineInRange("villageSearchRadius", 10.0, 1.0, 64.0);
        VILLAGE_SEARCH_HEIGHT = BUILDER.defineInRange("villageSearchHeight", 8.0, 1.0, 64.0);
        COOLDOWN_TICKS = BUILDER.defineInRange("cooldownTicks", 600, 0, 24000);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
