package com.jerrylu086.netherite_horse_armor.data;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import com.jerrylu086.netherite_horse_armor.config.ClothConfigHandler;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;

public record EasyCraftingCondition() implements ResourceCondition {
	public static final MapCodec<EasyCraftingCondition> CODEC = MapCodec.unit(EasyCraftingCondition::new);
	public static final ResourceConditionType<EasyCraftingCondition> TYPE = ResourceConditionType.create(NetheriteHorseArmor.asResource("easy_crafting"), CODEC);

	public static ResourceCondition create() {
		return new EasyCraftingCondition();
	}

	@Override
	public ResourceConditionType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean test(@Nullable HolderLookup.Provider registryLookup) {
		return NetheriteHorseArmor.clothConfigLoaded && ClothConfigHandler.getInstance().easyCrafting;
	}
}