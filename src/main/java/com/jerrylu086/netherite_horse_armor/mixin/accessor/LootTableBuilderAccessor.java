package com.jerrylu086.netherite_horse_armor.mixin.accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(LootTable.Builder.class)
public interface LootTableBuilderAccessor {
    @Accessor
    List<LootPool> getPools();
}
