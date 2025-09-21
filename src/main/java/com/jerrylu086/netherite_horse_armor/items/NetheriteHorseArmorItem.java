package com.jerrylu086.netherite_horse_armor.items;

import com.jerrylu086.netherite_horse_armor.Configuration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class NetheriteHorseArmorItem extends HorseArmorItem {
    public NetheriteHorseArmorItem(int protection, ResourceLocation path, Properties properties) {
        super(protection, path, properties);
    }

    @Override
    public int getProtection() {
        return Configuration.PROTECTION_VALUE.get();
    }
}
