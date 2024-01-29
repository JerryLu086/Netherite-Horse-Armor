package com.jerrylu086.netherite_horse_armor.config;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ClothConfigHandler {
    private static ModConfig loaded;

    public static ModConfig getInstance() {
        if (loaded == null)
            loaded = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        return loaded;
    }

    @Config(name = NetheriteHorseArmor.MOD_ID)
    public static class ModConfig implements ConfigData {

        @Comment("The weight you want the netherite horse armor to be in the loot table (bastion treasure). Requires /reload command to work if changed in game. Set to 0 to disable loot generation. (default: 8)")
        public int weight = 8;

        @Comment("The armor points you want for the netherite horse armor. (default: 13)")
        public int protectionValue = 13;

    }
}
