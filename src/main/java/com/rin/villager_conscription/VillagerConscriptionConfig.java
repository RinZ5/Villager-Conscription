package com.rin.villager_conscription;

import net.neoforged.neoforge.common.ModConfigSpec;

public class VillagerConscriptionConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue GUARD_SEARCH_RADIUS;
    public static final ModConfigSpec.DoubleValue VILLAGE_SEARCH_RADIUS;
    public static final ModConfigSpec.DoubleValue VILLAGE_SEARCH_HEIGHT;
    public static final ModConfigSpec.IntValue COOLDOWN_TICKS;
    public static final ModConfigSpec.DoubleValue GUARD_DROP_RATE;

    public static final ModConfigSpec.ConfigValue<String> HEAD_ITEM;
    public static final ModConfigSpec.ConfigValue<String> CHEST_ITEM;
    public static final ModConfigSpec.ConfigValue<String> LEGS_ITEM;
    public static final ModConfigSpec.ConfigValue<String> FEET_ITEM;
    public static final ModConfigSpec.ConfigValue<String> MAINHAND_ITEM;
    public static final ModConfigSpec.ConfigValue<String> OFFHAND_ITEM;

    static {
        BUILDER.push("General");

        GUARD_SEARCH_RADIUS = BUILDER.defineInRange("guardSearchRadius", 16.0, 1.0, 64.0);
        VILLAGE_SEARCH_RADIUS = BUILDER.defineInRange("villageSearchRadius", 10.0, 1.0, 64.0);
        VILLAGE_SEARCH_HEIGHT = BUILDER.defineInRange("villageSearchHeight", 8.0, 1.0, 64.0);
        COOLDOWN_TICKS = BUILDER.defineInRange("cooldownTicks", 600, 0, 24000);
        GUARD_DROP_RATE = BUILDER.defineInRange("guardDropRate", 100.0, 0, 100.0);

        BUILDER.pop();

        BUILDER.push("Equipment");

        HEAD_ITEM = BUILDER.define("headItem", "");
        CHEST_ITEM = BUILDER.define("chestItem", "");
        LEGS_ITEM = BUILDER.define("legsItem", "");
        FEET_ITEM = BUILDER.define("feetItem", "");
        MAINHAND_ITEM = BUILDER.define("mainhandItem", "minecraft:iron_sword");
        OFFHAND_ITEM = BUILDER.define("offhandItem", "minecraft:shield");

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
