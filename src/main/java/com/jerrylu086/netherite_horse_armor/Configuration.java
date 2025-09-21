package com.jerrylu086.netherite_horse_armor;

import net.neoforged.neoforge.common.ModConfigSpec;

public  class Configuration {
    public static ModConfigSpec COMMON;
    public static ModConfigSpec.IntValue WEIGHT;

    // No longer used, since Vanilla made this an attribute modifier thing ;-;
    // It now has 3 armor thoughness and 1 KB resistance though, which isn't too bad.

    // public static ModConfigSpec.IntValue PROTECTION_VALUE;
    public static ModConfigSpec.IntValue PROTECTION_VALUE;

    public static ModConfigSpec.BooleanValue EASY_CRAFTING;

    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        WEIGHT = BUILDER.comment("The weight you want the netherite horse armor to be in the loot table (bastion treasure). Requires /reload command to work if changed in game. Set to 0 to disable loot generation. (default: 8)").defineInRange("weight", 8, 0, Integer.MAX_VALUE);
        // PROTECTION_VALUE = BUILDER.comment("The armor points you want for the netherite horse armor. (default: 13)").defineInRange("protectionValue", 13, 1, 30);
        EASY_CRAFTING = BUILDER.comment("Whether you can upgrade a diamond horse armor into a netherite one with smithing table. (default: false)").define("easyCrafting", false);

        COMMON = BUILDER.build();
    }
}