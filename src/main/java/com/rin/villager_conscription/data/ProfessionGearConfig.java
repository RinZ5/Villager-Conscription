package com.rin.villager_conscription.data;

import com.google.gson.annotations.SerializedName;

public class ProfessionGearConfig {
    private String profession = "minecraft:none";
    private Equipment equipment = new Equipment();

    @SerializedName("drop_chance")
    private double dropChance = 0.8;

    @SerializedName("conversion_effect")
    private ConversionEffect conversionEffect = new ConversionEffect();

    public String getProfession() {
        return profession;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public double getDropChance() {
        return dropChance;
    }

    public ConversionEffect getConversionEffect() {
        return conversionEffect;
    }

    public static class Equipment {
        private String mainhand = "minecraft:iron_sword";
        private String offhand = "";
        private String head = "";
        private String chest = "";
        private String legs = "";
        private String feet = "";

        public String getMainhand() {
            return mainhand;
        }

        public String getOffhand() {
            return offhand;
        }

        public String getHead() {
            return head;
        }

        public String getChest() {
            return chest;
        }

        public String getLegs() {
            return legs;
        }

        public String getFeet() {
            return feet;
        }
    }

    public static class ConversionEffect {
        private String sound = "minecraft:item.armor.equip_iron";
        private String particle = "minecraft:scrape";

        @SerializedName("particle_count")
        private int particleCount = 15;

        public String getSound() {
            return sound;
        }

        public String getParticle() {
            return particle;
        }

        public int getParticleCount() {
            return particleCount;
        }
    }
}
