package com.jerrylu086.netherite_horse_armor.data;

import com.jerrylu086.netherite_horse_armor.Configuration;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

// A very useful copy-paste class lol
public class EasyCraftingCondition implements ICondition {
    public static final EasyCraftingCondition INSTANCE = new EasyCraftingCondition();
    public static final MapCodec<EasyCraftingCondition> CODEC = MapCodec.unit(new EasyCraftingCondition());

    @Override
    public boolean test(ICondition.IContext context) {
        return Configuration.EASY_CRAFTING.get();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}