package com.jerrylu086.netherite_horse_armor.data;

import com.google.gson.JsonObject;
import com.jerrylu086.netherite_horse_armor.Configuration;
import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

// A very useful copy-paste class lol
public class EasyCraftingCondition implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation(NetheriteHorseArmor.MOD_ID, "easy_crafting");
    public static final EasyCraftingCondition INSTANCE = new EasyCraftingCondition();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return Configuration.EASY_CRAFTING.get();
    }

    public static class Serializer implements IConditionSerializer<EasyCraftingCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, EasyCraftingCondition value) {
            // Yeah boi
        }

        @Override
        public EasyCraftingCondition read(JsonObject json) {
            return new EasyCraftingCondition();
        }

        @Override
        public ResourceLocation getID() {
            return EasyCraftingCondition.ID;
        }
    }
}