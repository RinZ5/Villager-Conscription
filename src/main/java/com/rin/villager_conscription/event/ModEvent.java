package com.rin.villager_conscription.event;

import com.rin.villager_conscription.data.ProfessionReloadListener;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = "villager_conscription")
public class ModEvent {
    private ModEvent() {}

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ProfessionReloadListener());
    }
}
