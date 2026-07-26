package com.rin.villager_conscription;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(VillagerConscription.MODID)
public class VillagerConscription {
    public static final String MODID = "villager_conscription";

    public VillagerConscription(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, VillagerConscriptionConfig.SPEC);
    }
}
