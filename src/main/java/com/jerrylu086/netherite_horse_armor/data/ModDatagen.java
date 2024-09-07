package com.jerrylu086.netherite_horse_armor.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var server = event.includeServer();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();
        var helper = event.getExistingFileHelper();

        generator.addProvider(server, new ModRecipes(output, registries));
        generator.addProvider(server, new AdvancementProvider(
                output, registries, helper, List.of(new ModAdvancements()))
        );
    }
}
