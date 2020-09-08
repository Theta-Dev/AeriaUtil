package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thetadev.aeriautil.data.BlockStateGenerator;
import thetadev.aeriautil.recipes.SieveRecipe;
import thetadev.aeriautil.registry.ICustomBlockState;
import thetadev.aeriautil.util.Helper;

import java.util.List;
import java.util.stream.Collectors;

public class BlockSieve extends BlockImpl implements ICustomBlockState
{
	private static final VoxelShape SHAPE_SIEVE = Block.makeCuboidShape(0, 0, 0, 16, 14, 16);

	public BlockSieve() {
		super("sieve", Block.Properties.create(Material.WOOD).hardnessAndResistance(0.6F).sound(SoundType.WOOD).notSolid());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_SIEVE;
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE_SIEVE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_SIEVE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(handIn);
		if(stack.isEmpty()) return ActionResultType.FAIL;

		List<ItemStack> output = SieveRecipe.getResult(stack, worldIn);
		if(output.isEmpty()) return ActionResultType.FAIL;

		BlockState blockState;
		if(stack.getItem() instanceof BlockItem) blockState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
		else blockState = Blocks.DIRT.getDefaultState();

		stack.shrink(1);
		if(!worldIn.isRemote) Helper.dropItems(output, worldIn, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
		Helper.spawnBlockParticles(worldIn, blockState, 5, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0.4, 0.1, 0.4);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void generateCustomBlockState(BlockStateGenerator generator) {
		generator.simpleBlock(this, generator.models().getExistingFile(generator.modLoc(getBaseName())));
	}
}
