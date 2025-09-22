package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.data.EasyCraftingCondition;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NetheriteHorseArmor.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        gen.addProvider(event.includeServer(), new ModRecipeProvider(output));
        gen.addProvider(event.includeServer(), new ModAdvancementProvider(output, lookupProvider, helper));

        gen.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
    }

    static class ModRecipeProvider extends RecipeProvider {
        public ModRecipeProvider(PackOutput output) {
            super(output);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
            ConditionalRecipe.builder()
                    .addCondition(EasyCraftingCondition.INSTANCE)
                    .addRecipe((c) -> netheriteSmithing(c,
                            Items.DIAMOND_HORSE_ARMOR,
                            RecipeCategory.MISC,
                            NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get()))
                    .generateAdvancement() // Weird, Fabric does not require this line while Forge does? Definitely ConditionalRecipe's fault.
                    .build(consumer, NetheriteHorseArmor.asResource("netherite_horse_armor_smithing"));
        }
    }

    static class ModAdvancementProvider extends ForgeAdvancementProvider {
        public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
            super(output, registries, existingFileHelper, List.of((registriez, consumer, helper) -> {
                // Why is it even called "recipeAdvancement"?
                var obtainNetheriteHorseArmor = Advancement.Builder.recipeAdvancement()
                        .parent(getAdvancement("nether/loot_bastion"))
                        .display(
                                NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get(),
                                Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.title"),
                                Component.translatable(NetheriteHorseArmor.MOD_ID + ".advancements.obtain.description"),
                                null,
                                FrameType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .rewards(AdvancementRewards.Builder.experience(50))
                        .addCriterion("netherite_horse_armor", InventoryChangeTrigger.TriggerInstance.hasItems(NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get()))
                        .save(consumer, NetheriteHorseArmor.MOD_ID + ":obtain_netherite_horse_armor");
            }));
        }

        static Advancement getAdvancement(String path) {
            return Advancement.Builder.advancement().build(new ResourceLocation(path));
        }
    }

    static class ModItemModelProvider extends ItemModelProvider {
        private static final String GENERATED = "item/generated";

        public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, NetheriteHorseArmor.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            generateFlatItem(NetheriteHorseArmor.NETHERITE_HORSE_ARMOR.get());
        }

        protected ItemModelBuilder generateFlatItem(Item item) {
            var path = ForgeRegistries.ITEMS.getKey(item).getPath();
            return withExistingParent(path, GENERATED).texture("layer0", "item/" + path);
        }
    }
}
