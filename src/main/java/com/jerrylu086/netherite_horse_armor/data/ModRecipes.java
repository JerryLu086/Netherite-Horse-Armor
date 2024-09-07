package com.jerrylu086.netherite_horse_armor.data;

import com.jerrylu086.netherite_horse_armor.NetheriteHorseArmor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipes extends RecipeProvider {
    public ModRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        var netheriteHorseArmor = NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get();
        var recipeId = NetheriteHorseArmor.MOD_ID + ":" + getItemName(netheriteHorseArmor) + "_easy_crafting";
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.DIAMOND_HORSE_ARMOR), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, netheriteHorseArmor)
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(recipeOutput.withConditions(new NetheriteHorseArmor.EasyCraftingCondition()), recipeId);
    }
}
