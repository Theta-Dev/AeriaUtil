package thetadev.aeriautil.event;

import net.minecraft.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.ModBlocks;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE, modid = AeriaUtil.MOD_ID)
public class ModEvents
{
	@SubscribeEvent
	public static void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
		if(event.getNewState().getBlock() != Blocks.OBSIDIAN) return;

		event.setNewState(ModBlocks.RAW_OBSIDIAN.getDefaultState());
	}
}
