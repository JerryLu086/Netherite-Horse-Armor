package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.data.EasyCraftingCondition;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod(NetheriteHorseArmor.MOD_ID)
public class NetheriteHorseArmor {
    public static final String MOD_ID = "netherite_horse_armor";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<MapCodec<? extends ICondition>> CODECS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, MOD_ID);

    public static final DeferredItem<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () ->
            new AnimalArmorItem(ArmorMaterials.NETHERITE, AnimalArmorItem.BodyType.EQUESTRIAN, false,
                    new Item.Properties().stacksTo(1).fireResistant()) {
                @Override
                public ResourceLocation getTexture() {
                    return ResourceLocation.fromNamespaceAndPath(MOD_ID, super.getTexture().getPath());
                }
            }
    );

    public static final Supplier<MapCodec<? extends ICondition>> EASY_CRAFTING = CODECS.register("easy_crafting", () -> EasyCraftingCondition.CODEC);

    public NetheriteHorseArmor(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CODECS.register(modEventBus);

        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);
        modEventBus.addListener(this::addToTab);

        NeoForge.EVENT_BUS.register(this);
    }

    private void addToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(
                    new ItemStack(Items.DIAMOND_HORSE_ARMOR),
                    new ItemStack(NETHERITE_HORSE_ARMOR.get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    // From Quark mod by Team Violet Moon. GitHub: https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/tools/module/ColorRunesModule.java#L177
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if(!(event.getName().equals(BuiltInLootTables.BASTION_TREASURE.location()) && Configuration.WEIGHT.get() > 0))
            return;

        var entry = LootItem.lootTableItem(NETHERITE_HORSE_ARMOR.get()).setWeight(Configuration.WEIGHT.get()).setQuality(1).build();
        var pools = ((LootTableAccessor) event.getTable()).getPools();

        if (pools != null && !pools.isEmpty()) {
            var firstPool = pools.get(0);
            var entries = ((LootPoolAccessor) firstPool).getEntries();

            ImmutableList<LootPoolEntryContainer> newEntries =
                    ImmutableList.<LootPoolEntryContainer>builder().addAll(entries).add(entry).build();
            ((LootPoolAccessor) firstPool).setEntries(newEntries);

            LOGGER.info("Successfully modified loot table: '{}'", BuiltInLootTables.BASTION_TREASURE.location());
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
