package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.data.EasyCraftingCondition;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NetheriteHorseArmor.MOD_ID)
public class ModDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        gen.addProvider(event.includeServer(), new ModRecipeProvider(output, registries));
        gen.addProvider(event.includeServer(), new ModAdvancementProvider(output, registries, helper));
    }

    static class ModRecipeProvider extends RecipeProvider {
        public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void buildRecipes(RecipeOutput output) {
            netheriteSmithingAlt(output.withConditions(EasyCraftingCondition.INSTANCE), Items.DIAMOND_HORSE_ARMOR, RecipeCategory.MISC, NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get());
        }

        // Bruh
        protected static void netheriteSmithingAlt(RecipeOutput recipeOutput, Item ingredientItem, RecipeCategory category, Item resultItem) {
            SmithingTransformRecipeBuilder.smithing(
                            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ingredientItem), Ingredient.of(Items.NETHERITE_INGOT), category, resultItem
                    )
                    .unlocks("has_netherite_ingot", RecipeProvider.has(Items.NETHERITE_INGOT))
                    .save(recipeOutput, NetheriteHorseArmor.asResource(getItemName(resultItem) + "_smithing"));
        }
    }

    static class ModAdvancementProvider extends AdvancementProvider {
        public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
            super(output, registries, existingFileHelper, List.of((registriez, consumer, helper) -> {
                var nha = NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get();
                var obtainNetheriteHorseArmor = Advancement.Builder.advancement()
                        .parent(getAdvancement("nether/loot_bastion"))
                        .display(
                                nha,
                                Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.title"),
                                Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.description"),
                                null,
                                AdvancementType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .addCriterion("netherite_horse_armor", InventoryChangeTrigger.TriggerInstance.hasItems(nha))
                        .save(consumer, NetheriteHorseArmor.MOD_ID + ":obtain_netherite_horse_armor");
            }));
        }
    }

    static AdvancementHolder getAdvancement(String path) {
        return Advancement.Builder.advancement().build(ResourceLocation.parse(path));
    }
}
