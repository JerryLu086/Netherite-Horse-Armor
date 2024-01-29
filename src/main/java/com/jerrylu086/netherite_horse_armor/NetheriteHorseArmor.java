package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

@SuppressWarnings("unused")
@Mod(NetheriteHorseArmor.MOD_ID)
public class NetheriteHorseArmor {
    public static final String MOD_ID = "netherite_horse_armor";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () ->
            new HorseArmorItem(13, new ResourceLocation(MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png"),
                    new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC).fireResistant()){
                @Override
                public int getProtection() {
                    return Configuration.PROTECTION_VALUE.get();
                }
            });

    public NetheriteHorseArmor() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class Configuration {
        public static ForgeConfigSpec COMMON;
        public static ForgeConfigSpec.IntValue WEIGHT;
        public static ForgeConfigSpec.IntValue PROTECTION_VALUE;

        static {
            ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
            WEIGHT = BUILDER.comment("The weight you want the netherite horse armor to be in the loot table (bastion treasure). Requires /reload to work command if changed in game. Set to 0 to disable loot generation. (default: 8)").defineInRange("weight", 8, 0, Integer.MAX_VALUE);
            PROTECTION_VALUE = BUILDER.comment("The armor points you want for the netherite horse armor. (default: 13)").defineInRange("protectionValue", 13, 1, 30);
            COMMON = BUILDER.build();
        }
    }

    // From Quark mod by Team Violet Moon. GitHub: https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/content/tools/module/ColorRunesModule.java#L177
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if(!(event.getName().equals(LootTables.BASTION_TREASURE) && Configuration.WEIGHT.get() > 0))
            return;

        LootEntry entry = ItemLootEntry.lootTableItem(NETHERITE_HORSE_ARMOR.get()).setWeight(Configuration.WEIGHT.get()).setQuality(1).build();
        List<LootPool> pools = ((LootTableAccessor)event.getTable()).getPools();

        if (pools != null && !pools.isEmpty())
            ((LootPoolAccessor)pools.get(0)).getEntries().add(entry);
    }
}
