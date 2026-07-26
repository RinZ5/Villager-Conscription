package com.rin.villager_conscription.data;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class ProfessionReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<ResourceLocation, ProfessionGearConfig> GEAR_CONFIGS = new HashMap<>();

    public ProfessionReloadListener() {
        super(GSON, "professions");
    }

    @Override
    protected void apply(
        Map<ResourceLocation, JsonElement> elements,
        ResourceManager manager,
        ProfilerFiller profiler
    ) {
        GEAR_CONFIGS.clear();

        elements.forEach((location, jsonElement) -> {
            ProfessionGearConfig config = GSON.fromJson(jsonElement, ProfessionGearConfig.class);
            ResourceLocation professionId = ResourceLocation.parse(config.getProfession());
            GEAR_CONFIGS.put(professionId, config);
        });
    }

    public static ProfessionGearConfig getConfig(ResourceLocation id) {
        return GEAR_CONFIGS.get(id);
    }

    public static boolean hasConfig(ResourceLocation id) {
        return GEAR_CONFIGS.containsKey(id);
    }
}
