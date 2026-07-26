package com.rin.villager_conscription.mixin;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.ModList;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

import static com.rin.villager_conscription.VillagerConscriptionConfig.GUARD_SEARCH_RADIUS;
import static com.rin.villager_conscription.VillagerConscriptionConfig.VILLAGE_SEARCH_RADIUS;
import static com.rin.villager_conscription.VillagerConscriptionConfig.VILLAGE_SEARCH_HEIGHT;
import static com.rin.villager_conscription.VillagerConscriptionConfig.COOLDOWN_TICKS;
import static com.rin.villager_conscription.VillagerConscriptionConfig.GUARD_DROP_RATE;
import static com.rin.villager_conscription.VillagerConscriptionConfig.HEAD_ITEM;
import static com.rin.villager_conscription.VillagerConscriptionConfig.CHEST_ITEM;
import static com.rin.villager_conscription.VillagerConscriptionConfig.LEGS_ITEM;
import static com.rin.villager_conscription.VillagerConscriptionConfig.FEET_ITEM;
import static com.rin.villager_conscription.VillagerConscriptionConfig.MAINHAND_ITEM;
import static com.rin.villager_conscription.VillagerConscriptionConfig.OFFHAND_ITEM;

@Mixin(Villager.class)
public abstract class VillagerSummonGolemMixin {

    @Unique
    private static final Set<String> ALLOWED_PROFS = Set.of("none", "nitwit");

    @Inject(method = "spawnGolemIfNeeded", at = @At("HEAD"), cancellable = true)
    private void villager_conscription$redirectGolemToGuard(
            ServerLevel world,
            long time,
            int requiredCount,
            CallbackInfo ci) {
        double villageSearchRadius = VILLAGE_SEARCH_RADIUS.get();
        double villageSearchHeight = VILLAGE_SEARCH_HEIGHT.get();

        if (!ModList.get().isLoaded("guardvillagers")) {
            return;
        }

        Villager self = (Villager) (Object) this;

        if (!self.getBrain().isActive(Activity.PANIC) || hasGuardsNearby(world, self)) {
            return;
        }

        AABB villageBounds = self.getBoundingBox().inflate(
                villageSearchRadius,
                villageSearchHeight,
                villageSearchRadius
        );
        List<Villager> allNearbyVillagers = world.getEntitiesOfClass(
                Villager.class,
                villageBounds,
                v -> true
        );

        List<Villager> candidates = filterCandidates(allNearbyVillagers, self);

        if (candidates.isEmpty()) {
            return;
        }

        Villager target = candidates.get(self.getRandom().nextInt(candidates.size()));
        convertVillagerToGuard(target, world);
        applyCooldowns(allNearbyVillagers);

        ci.cancel();
    }

    @Unique
    private boolean hasGuardsNearby(ServerLevel world, Villager self) {
        double guardSearchRadius = GUARD_SEARCH_RADIUS.get();

        AABB searchBox = self.getBoundingBox().inflate(
                guardSearchRadius,
                guardSearchRadius / 2.0,
                guardSearchRadius
        );
        List<Guard> existingGuards = world.getEntitiesOfClass(Guard.class, searchBox, g -> true);
        return !existingGuards.isEmpty();
    }

    @Unique
    private List<Villager> filterCandidates(List<Villager> villagers, Villager self) {
        return villagers.stream().filter(v -> !v.isBaby()).filter(v -> v != self)
                .filter(v -> ALLOWED_PROFS.contains(v.getVillagerData().getProfession().name()))
                .toList();
    }

    @Unique
    private void applyCooldowns(List<Villager> villagers) {
        long cooldownTicks = COOLDOWN_TICKS.get();

        villagers.stream().limit(5).forEach(
                v -> v.getBrain().setMemoryWithExpiry(
                        MemoryModuleType.GOLEM_DETECTED_RECENTLY,
                        true,
                        cooldownTicks
                )
        );
    }

    @Unique
    private static void convertVillagerToGuard(Villager villager, ServerLevel world) {
        Guard guard = GuardEntityType.GUARD.get().create(world);

        if (guard == null) {
            return;
        }

        guard.copyPosition(villager);
        guard.setVariant(villager.getVariant().toString());
        guard.setPersistenceRequired();
        guard.setCustomName(villager.getCustomName());
        guard.setCustomNameVisible(villager.isCustomNameVisible());

        equipGuard(guard);

        villager.releasePoi(MemoryModuleType.HOME);
        villager.releasePoi(MemoryModuleType.JOB_SITE);
        villager.releasePoi(MemoryModuleType.MEETING_POINT);

        villager.discard();
        world.addFreshEntity(guard);

        playConversionEffects(guard, world);
    }

    @Unique
    private static ItemStack getConfigItem(String itemId) {
        if (itemId.isBlank() || itemId.equals("minecraft:air")) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId)));
    }

    @Unique
    private static void equipGuard(Guard guard) {
        float guardDropRate = GUARD_DROP_RATE.get().floatValue();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            guard.setDropChance(slot, guardDropRate);
        }

        guard.setItemSlot(EquipmentSlot.HEAD, getConfigItem(HEAD_ITEM.get()));
        guard.setItemSlot(EquipmentSlot.CHEST, getConfigItem(CHEST_ITEM.get()));
        guard.setItemSlot(EquipmentSlot.LEGS, getConfigItem(LEGS_ITEM.get()));
        guard.setItemSlot(EquipmentSlot.FEET, getConfigItem(FEET_ITEM.get()));
        guard.setItemSlot(EquipmentSlot.MAINHAND, getConfigItem(MAINHAND_ITEM.get()));
        guard.setItemSlot(EquipmentSlot.OFFHAND, getConfigItem(OFFHAND_ITEM.get()));
    }

    @Unique
    private static void playConversionEffects(Guard guard, ServerLevel world) {
        world.playSound(
                null,
                guard.blockPosition(),
                SoundEvents.ARMOR_EQUIP_IRON.value(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );

        world.sendParticles(
                ParticleTypes.SCRAPE,
                guard.getX(),
                guard.getY() + 1.0,
                guard.getZ(),
                15,
                0.3,
                0.5,
                0.3,
                0.1
        );
    }
}
