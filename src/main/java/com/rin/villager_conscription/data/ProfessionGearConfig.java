package com.rin.villager_conscription.data;

import com.google.gson.annotations.SerializedName;

public class ProfessionGearConfig {
    private String profession = "minecraft:none";
    private Equipment equipment = new Equipment();

    @SerializedName("drop_chance")
    private double dropChance = 0.8;

    public String getProfession() {
        return profession;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public double getDropChance() {
        return dropChance;
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
}
