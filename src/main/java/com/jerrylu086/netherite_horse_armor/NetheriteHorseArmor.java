package com.jerrylu086.netherite_horse_armor;

import com.google.common.collect.ImmutableList;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;
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
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(NetheriteHorseArmor.MOD_ID)
public class NetheriteHorseArmor {
    public static final String MOD_ID = "netherite_horse_armor";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () ->
            new HorseArmorItem(13, new ResourceLocation(MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png"),
                    new Item.Properties().stacksTo(1).fireResistant()) {
                @Override
                public int getProtection() {
                    return Configuration.PROTECTION_VALUE.get();
                }
            });

    public NetheriteHorseArmor(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);
        modEventBus.addListener(this::addToTab);

        NeoForge.EVENT_BUS.register(this);
    }

    private void addToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.getEntries().putAfter(
                    new ItemStack(Items.DIAMOND_HORSE_ARMOR),
                    new ItemStack(NETHERITE_HORSE_ARMOR.get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    public static class Configuration {
        public static ModConfigSpec COMMON;
        public static ModConfigSpec.IntValue WEIGHT;
        public static ModConfigSpec.IntValue PROTECTION_VALUE;

        static {
            ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
            WEIGHT = BUILDER.comment("The weight you want the netherite horse armor to be in the loot table (bastion treasure). Requires /reload command to work if changed in game. Set to 0 to disable loot generation. (default: 8)").defineInRange("weight", 8, 0, Integer.MAX_VALUE);
            PROTECTION_VALUE = BUILDER.comment("The armor points you want for the netherite horse armor. (default: 13)").defineInRange("protectionValue", 13, 1, 30);
            COMMON = BUILDER.build();
        }
    }

    // From Quark mod by Team Violet Moon. GitHub: https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/tools/module/ColorRunesModule.java#L177
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if(!(event.getName().equals(BuiltInLootTables.BASTION_TREASURE) && Configuration.WEIGHT.get() > 0))
            return;

        var entry = LootItem.lootTableItem(NETHERITE_HORSE_ARMOR.get()).setWeight(Configuration.WEIGHT.get()).setQuality(1).build();
        var pools = ((LootTableAccessor)event.getTable()).getPools();

        if (pools != null && !pools.isEmpty()) {
            var firstPool = pools.get(0);
            var entries = ((LootPoolAccessor)firstPool).getEntries();

            ImmutableList<LootPoolEntryContainer> newEntries =
                    ImmutableList.<LootPoolEntryContainer>builder().addAll(entries).add(entry).build();
            ((LootPoolAccessor)firstPool).setEntries(newEntries);

            LOGGER.info("Successfully modified loot table: '{}'", BuiltInLootTables.BASTION_TREASURE);
        }
    }
}
