package com.jerrylu086.netherite_horse_armor.mixin.accessor;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Mutable
    @Accessor
    void setEntries(List<LootEntry> entries);
    @Accessor
    List<LootEntry> getEntries();
}
