package com.jerrylu086.netherite_horse_armor.items;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import com.jerrylu086.netherite_horse_armor.config.ClothConfigHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class NetheriteHorseArmorItem extends HorseArmorItem {
    public NetheriteHorseArmorItem(int protection, String identifier, Properties properties) {
        super(protection, identifier, properties);
    }

    @Override
    public ResourceLocation getTexture() {
        return NetheriteHorseArmor.asResource("textures/entity/horse/armor/horse_armor_netherite.png");
    }

    @Override
    public int getProtection() {
        return NetheriteHorseArmor.clothConfigLoaded ? ClothConfigHandler.getInstance().protectionValue : 13;
    }
}
