package com.jerrylu086.netherite_horse_armor.data;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class ModAdvancements implements AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        var obtainNetheriteHorseArmor = Advancement.Builder.advancement()
                .parent(getAdvancement("nether/loot_bastion"))
                .display(
                        NetheriteHorseArmor.NETHERITE_HORSE_ARMOR,
                        Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.title"),
                        Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("netherite_horse_armor", InventoryChangeTrigger.TriggerInstance.hasItems(NetheriteHorseArmor.NETHERITE_HORSE_ARMOR))
                .save(saver, NetheriteHorseArmor.MOD_ID + ":obtain_netherite_horse_armor");
    }

    protected static AdvancementHolder getAdvancement(String path) {
        return Advancement.Builder.advancement().build(ResourceLocation.parse(path));
    }
}
