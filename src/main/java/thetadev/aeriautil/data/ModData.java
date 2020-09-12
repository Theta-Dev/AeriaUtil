package thetadev.aeriautil.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import thetadev.aeriautil.data.recipes.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModData {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if(event.includeServer()) {
			generator.addProvider(new BlockLootProvider(generator));

			generator.addProvider(new CraftingProvider(generator));
			generator.addProvider(new SmeltingProvider(generator));
			generator.addProvider(new SieveProvider(generator));
			generator.addProvider(new HammerProvider(generator));
			generator.addProvider(new WashingProvider(generator));

			generator.addProvider(new BlockTagProvider(generator));
			generator.addProvider(new ItemTagProvider(generator));
		}

		if(event.includeClient()) {
			generator.addProvider(new BlockStateGenerator(generator, fileHelper));
			generator.addProvider(new ItemModelGenerator(generator, fileHelper));
		}
	}
}