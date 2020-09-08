package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thetadev.aeriautil.data.BlockStateGenerator;
import thetadev.aeriautil.data.tags.ModItemTags;
import thetadev.aeriautil.registry.ICustomBlockState;

import javax.annotation.Nullable;

public class BlockLeafPile extends BlockImpl implements ICustomBlockState
{
	public BlockLeafPile() {
		super("leaf_pile", Block.Properties.create(Material.ORGANIC, MaterialColor.GREEN).hardnessAndResistance(0.5F, 2.5F).sound(SoundType.PLANT));
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		player.addStat(Stats.BLOCK_MINED.get(this));
		player.addExhaustion(0.005F);

		if(stack.getItem().getTags().contains(ModItemTags.HAMMERS.getId())) {
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
		else spawnDrops(state, world, pos, te, player, stack);
	}

	@Override
	public void generateCustomBlockState(BlockStateGenerator generator) {
		generator.simpleBlock(this, generator.models().getExistingFile(generator.modLoc(getBaseName())));
	}
}
