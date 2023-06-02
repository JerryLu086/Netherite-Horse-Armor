package com.jerrylu086.netherite_horse_armor.mixin.accessor;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(LootTable.class)
public interface LootTableAccessor {
    @Accessor
    List<LootPool> getPools();
}
