package com.jerrylu086.netherite_horse_armor;

import com.jerrylu086.netherite_horse_armor.config.ClothConfigHandler;
import com.jerrylu086.netherite_horse_armor.config.ClothConfigHandler.ModConfig;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootPoolAccessor;
import com.jerrylu086.netherite_horse_armor.mixin.accessor.LootTableBuilderAccessor;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetheriteHorseArmor implements ModInitializer {
	public static final String MOD_ID = "netherite_horse_armor";
	public static final Logger LOGGER = LogManager.getLogger();

	private static boolean clothConfigLoaded;

	public static final Item NETHERITE_HORSE_ARMOR = new HorseArmorItem(13, "netherite",
			new FabricItemSettings().stacksTo(1).fireResistant().tab(CreativeModeTab.TAB_COMBAT)) {

		@Override
		public ResourceLocation getTexture() {
			return asResource("textures/entity/horse/armor/horse_armor_netherite.png");
		}

		@Override
		public int getProtection() {
			return clothConfigLoaded ? ClothConfigHandler.getInstance().protectionValue : 13;
		}
	};

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
			clothConfigLoaded = true;
			AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		}

		Registry.register(Registry.ITEM, asResource( "netherite_horse_armor"), NETHERITE_HORSE_ARMOR);

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(!id.equals(BuiltInLootTables.BASTION_TREASURE))
				return;

			List<LootPool> pools = ((LootTableBuilderAccessor) tableBuilder).getPools();
			LootPoolEntryContainer entry = LootItem.lootTableItem(NETHERITE_HORSE_ARMOR)
					.setWeight(clothConfigLoaded ? ClothConfigHandler.getInstance().weight : 8)
					.setQuality(1).build();

			if (pools != null && !pools.isEmpty()) {
				LootPool firstPool = pools.get(0);
				LootPoolEntryContainer[] entries = ((LootPoolAccessor)firstPool).getEntries();

				LootPoolEntryContainer[] newEntries = new LootPoolEntryContainer[entries.length + 1];
				System.arraycopy(entries, 0, newEntries, 0, entries.length);

				newEntries[entries.length] = entry;
				((LootPoolAccessor)firstPool).setEntries(newEntries);

				LOGGER.info("Successfully modified loot table: '{}'", BuiltInLootTables.BASTION_TREASURE);
			}
		});
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}