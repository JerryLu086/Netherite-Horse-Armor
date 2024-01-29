package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@Mod(NetheriteHorseArmor.MOD_ID)
public class NetheriteHorseArmor {
    public static final String MOD_ID = "netherite_horse_armor";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () ->
            new HorseArmorItem(13, new ResourceLocation(MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png"),
                    new Item.Properties().stacksTo(1).fireResistant()) {
                @Override
                public int getProtection() {
                    return Configuration.PROTECTION_VALUE.get();
                }
            });

    public NetheriteHorseArmor() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);
        modEventBus.addListener(this::addToTab);

        MinecraftForge.EVENT_BUS.register(this);
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
        public static ForgeConfigSpec COMMON;
        public static ForgeConfigSpec.IntValue WEIGHT;
        public static ForgeConfigSpec.IntValue PROTECTION_VALUE;

        static {
            ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
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

            var newEntries = new LootPoolEntryContainer[entries.length + 1];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);

            newEntries[entries.length] = entry;
            ((LootPoolAccessor)firstPool).setEntries(newEntries);
        }
    }
}
