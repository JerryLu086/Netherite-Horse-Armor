package com.jerrylu086.netherite_horse_armor.mixin.accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Mutable
    @Accessor
    void setEntries(List<LootPoolEntryContainer> entries);
    @Accessor
    List<LootPoolEntryContainer> getEntries();
}
