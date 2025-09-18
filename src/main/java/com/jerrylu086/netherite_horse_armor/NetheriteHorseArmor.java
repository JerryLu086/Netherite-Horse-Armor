package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.data.EasyCraftingCondition;
import com.jerrylu086.netherite_horse_armor.items.NetheriteHorseArmorItem;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableAccessor;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Mod(NetheriteHorseArmor.MOD_ID)
public class NetheriteHorseArmor {
    public static final String MOD_ID = "netherite_horse_armor";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () ->
            new NetheriteHorseArmorItem(13, asResource("textures/entity/horse/armor/horse_armor_netherite.png"),
                    new Item.Properties().stacksTo(1).fireResistant()));

    public NetheriteHorseArmor() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerEvent);

        ITEMS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON);
        modEventBus.addListener(this::addToTab);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerEvent(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(EasyCraftingCondition.Serializer.INSTANCE);
        }
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

            LOGGER.info("Successfully modified loot table: '{}'", BuiltInLootTables.BASTION_TREASURE);
        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
