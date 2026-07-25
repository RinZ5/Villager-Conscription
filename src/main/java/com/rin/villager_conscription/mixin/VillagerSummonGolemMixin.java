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
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModList;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

@Mixin(Villager.class)
public abstract class VillagerSummonGolemMixin {

    @Unique
    private static final Set<String> ALLOWED_PROFS = Set.of("none", "nitwit");

    @Unique
    private static final double GUARD_CHECK_RADIUS = 16.0;

    @Unique
    private static final double VILLAGE_SEARCH_RADIUS = 10.0;

    @Unique
    private static final double VILLAGE_SEARCH_HEIGHT = 8.0;

    @Inject(method = "spawnGolemIfNeeded", at = @At("HEAD"), cancellable = true)
    private void villager_conscription$redirectGolemToGuard(ServerLevel world, long time, int requiredCount,
            CallbackInfo ci) {
        if (!ModList.get().isLoaded("guardvillagers")) {
            return;
        }

        Villager self = (Villager) (Object) this;

        if (!self.getBrain().isActive(Activity.PANIC) || hasGuardsNearby(world, self)) {
            return;
        }

        AABB villageBounds = self.getBoundingBox().inflate(VILLAGE_SEARCH_RADIUS, VILLAGE_SEARCH_HEIGHT,
                VILLAGE_SEARCH_RADIUS);
        List<Villager> allNearbyVillagers = world.getEntitiesOfClass(Villager.class, villageBounds, v -> true);

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
        AABB searchBox = self.getBoundingBox().inflate(GUARD_CHECK_RADIUS, GUARD_CHECK_RADIUS / 2.0,
                GUARD_CHECK_RADIUS);
        List<Guard> existingGuards = world.getEntitiesOfClass(Guard.class, searchBox, g -> true);
        return !existingGuards.isEmpty();
    }

    @Unique
    private List<Villager> filterCandidates(List<Villager> villagers, Villager self) {
        return villagers.stream()
                .filter(v -> !v.isBaby())
                .filter(v -> v != self)
                .filter(v -> ALLOWED_PROFS.contains(v.getVillagerData().getProfession().name()))
                .toList();
    }

    @Unique
    private void applyCooldowns(List<Villager> villagers) {
        villagers.stream()
                .limit(5)
                .forEach(v -> v.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 600L));
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

        guard.setDropChance(EquipmentSlot.HEAD, 100.0F);
        guard.setDropChance(EquipmentSlot.CHEST, 100.0F);
        guard.setDropChance(EquipmentSlot.FEET, 100.0F);
        guard.setDropChance(EquipmentSlot.LEGS, 100.0F);
        guard.setDropChance(EquipmentSlot.MAINHAND, 100.0F);
        guard.setDropChance(EquipmentSlot.OFFHAND, 100.0F);

        equipGuard(guard);

        villager.releasePoi(MemoryModuleType.HOME);
        villager.releasePoi(MemoryModuleType.JOB_SITE);
        villager.releasePoi(MemoryModuleType.MEETING_POINT);

        villager.discard();
        world.addFreshEntity(guard);

        playConversionEffects(guard, world);
    }

    @Unique
    private static void equipGuard(Guard guard) {
        guard.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    @Unique
    private static void playConversionEffects(Guard guard, ServerLevel world) {
        world.playSound(
                null,
                guard.blockPosition(),
                SoundEvents.ARMOR_EQUIP_IRON.value(),
                SoundSource.NEUTRAL,
                1.0F,
                1.0F);

        world.sendParticles(
                ParticleTypes.SCRAPE,
                guard.getX(), guard.getY() + 1.0, guard.getZ(),
                15,
                0.3,
                0.5,
                0.3,
                0.1);
    }
}