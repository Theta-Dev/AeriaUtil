package thetadev.aeriautil.event;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.ModBlocks;
import thetadev.aeriautil.recipes.WashingRecipe;
import thetadev.aeriautil.util.Helper;

import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE, modid = AeriaUtil.MOD_ID)
public class WashingEvents
{
	private static final String TAG_WASHING = "AeriaWashing";

	@SubscribeEvent
	public static void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
		if(event.getNewState().getBlock() == Blocks.OBSIDIAN) {
			event.setNewState(ModBlocks.RAW_OBSIDIAN.getDefaultState());
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getWorld().isRemote || !(event.getEntity() instanceof ItemEntity)) return;

		ItemEntity itemEntity = (ItemEntity)event.getEntity();
		ItemStack stack = itemEntity.getItem();

		if(WashingRecipe.getRecipes(stack, event.getWorld()).isEmpty()) return;

		itemEntity.getPersistentData().putInt(TAG_WASHING, 100);
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		if(!event.side.isServer()) return;
		ServerWorld world = (ServerWorld) event.world;

		List<Entity> washingItems = world.getEntities(EntityType.ITEM, e->true);

		for(Entity entity : washingItems) {
			ItemEntity itemEntity = (ItemEntity)entity;

			int washingTicks = itemEntity.getPersistentData().getInt(TAG_WASHING);
			if(washingTicks <= 0) continue;

			if(itemEntity.isInWaterOrBubbleColumn() && entity.getMotion().length() > 0.1) {
				washingTicks--;

				if(washingTicks == 0) {
					List<ItemStack> result = WashingRecipe.getResult(itemEntity.getItem(), world);
					Helper.dropItems(result, world, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ());
					itemEntity.remove();
				}
				else itemEntity.getPersistentData().putInt(TAG_WASHING, washingTicks);
			}
		}
	}
}
