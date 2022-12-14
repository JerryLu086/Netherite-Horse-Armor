package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
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
                    new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC).fireResistant()));

    public NetheriteHorseArmor() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class Configuration {
        public static ForgeConfigSpec COMMON;
        public static ForgeConfigSpec.BooleanValue BUILTIN_LOOT_GENERATION;
        public static ForgeConfigSpec.IntValue WEIGHT;

        static {
            ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
            BUILTIN_LOOT_GENERATION = BUILDER.push("Obtaining Netherite Horse Armor").comment("Should the game use builtin loot generation(in the bastion treasure loot table) for the horse armor? (this mod doesn't use data pack due to compatibility problems)").define("builtinLootGeneration", true);
            WEIGHT = BUILDER.comment("If use the builtin loot generation, how much the weight should be? (default: 8, about 20% chance)").defineInRange("weight", 8, 1, Integer.MAX_VALUE);
            COMMON = BUILDER.build();
        }
    }

    // From Quark mod by Team Violet Moon. GitHub: https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/tools/module/ColorRunesModule.java#L177
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if(!(event.getName().equals(BuiltInLootTables.BASTION_TREASURE) && Configuration.BUILTIN_LOOT_GENERATION.get()))
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
