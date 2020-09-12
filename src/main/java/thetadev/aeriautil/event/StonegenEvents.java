package thetadev.aeriautil.event;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import thetadev.aeriautil.AeriaUtil;

import java.util.List;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE, modid = AeriaUtil.MOD_ID)
public class StonegenEvents
{
	@SubscribeEvent
	public static void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
		if(!(event.getWorld() instanceof ServerWorld)) return;
		ServerWorld world = (ServerWorld) event.getWorld();

		GeneratorType generator = GeneratorType.getGenerator(event.getNewState().getBlock());
		if(generator == null) return;

		event.setNewState(generator.generateBlock(world, event.getPos()).getDefaultState());
	}

	@SubscribeEvent
	public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
		if(!(event.getWorld() instanceof ServerWorld)) return;
		ServerWorld world = (ServerWorld) event.getWorld();

		if(!(event.getState().getBlock() instanceof FlowingFluidBlock)) return;
		FlowingFluidBlock fluidBlock = (FlowingFluidBlock) event.getState().getBlock();
		if(!fluidBlock.getFluid().isIn(FluidTags.LAVA)) return;

		BlockPos pos = event.getPos();
		if(world.getBlockState(pos.down()).getBlock() == Blocks.SOUL_SAND) {
			boolean hasIce = false;

			for(Direction dir : Direction.values()) {
				if(world.getBlockState(pos.offset(dir)).getBlock() == Blocks.BLUE_ICE) {
					hasIce = true;
					break;
				}
			}

			if(hasIce) {
				world.setBlockState(pos, GeneratorType.BASALT.generateBlock(world, pos).getDefaultState());
				world.playEvent(1501, pos, 0);
			}
		}
	}

	private enum GeneratorType
	{
		COBBLE("cobble_gen", Blocks.COBBLESTONE),
		STONE("stone_gen", Blocks.STONE),
		OBSIDIAN("obsidian_gen", Blocks.OBSIDIAN),
		BASALT("basalt_gen", null);

		final String resourceName;
		final Block genBlock;

		GeneratorType(String resourceName, Block genBlock) {
			this.resourceName = resourceName;
			this.genBlock = genBlock;
		}

		ResourceLocation getLootTableLoc() {
			return AeriaUtil.loc(resourceName);
		}

		static GeneratorType getGenerator(Block block) {
			for(GeneratorType type : values()) {
				if(type.genBlock == block && type.genBlock != null) return type;
			}
			return null;
		}

		Block generateBlock(ServerWorld world, BlockPos pos) {
			LootTable lootTable = world.getServer().getLootTableManager().getLootTableFromLocation(getLootTableLoc());
			LootContext ctx = new LootContext.Builder(world)
					.withParameter(LootParameters.POSITION, pos)
					.build(LootParameterSets.COMMAND);
			List<ItemStack> loot = lootTable.generate(ctx);

			for(ItemStack stack : loot) {
				if(stack.getItem() instanceof BlockItem) {
					return ((BlockItem) stack.getItem()).getBlock();
				}
			}
			return Blocks.AIR;
		}
	}
}
