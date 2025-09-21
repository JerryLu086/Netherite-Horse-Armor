package com.jerrylu086.netherite_horse_armor.mixin.accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.ImmutableList;

@SuppressWarnings("unused")
@Mixin(LootTable.Builder.class)
public interface LootTableBuilderAccessor {
    @Mutable
    @Accessor
    void setPools(ImmutableList.Builder<LootPool> var1);
    @Accessor
    ImmutableList.Builder<LootPool> getPools();
}
