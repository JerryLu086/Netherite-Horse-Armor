package com.jerrylu086.netherite_horse_armor.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;

public class AnimalArmorItem extends Item {
    public enum BodyType {
        EQUESTRIAN // Add others if needed
    }

    private final ArmorMaterial material;
    private final BodyType bodyType;

    public AnimalArmorItem(ArmorMaterial material, BodyType bodyType, boolean customModel, Settings settings) {
        super((Properties) settings);
        this.material = material;
        this.bodyType = bodyType;
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.parse("textures/entity/horse/armor/horse_armor_netherite.png");
    }

    public ArmorMaterial getArmorMaterial() {
        return material;
    }

    public BodyType getBodyType() {
        return bodyType;
    }
}
