package com.jerrylu086.netherite_horse_armor.data;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ModDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModAdvancementProvider::new);
    }

    static class ModRecipeProvider extends FabricRecipeProvider {
        public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void buildRecipes(RecipeOutput exporter) {
            FabricRecipeProvider.netheriteSmithing(this.withConditions(exporter, EasyCraftingCondition.create()), Items.DIAMOND_HORSE_ARMOR, RecipeCategory.MISC, NetheriteHorseArmor.NETHERITE_HORSE_ARMOR);
        }
    }

    static class ModAdvancementProvider extends FabricAdvancementProvider {
        protected ModAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
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
                    .save(consumer, NetheriteHorseArmor.MOD_ID + ":obtain_netherite_horse_armor");
        }

        static AdvancementHolder getAdvancement(String path) {
            return Advancement.Builder.advancement().build(ResourceLocation.parse(path));
        }
    }
}
