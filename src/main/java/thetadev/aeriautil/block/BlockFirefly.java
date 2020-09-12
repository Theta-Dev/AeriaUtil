package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootTable;
import thetadev.aeriautil.data.BlockLootProvider;
import thetadev.aeriautil.data.BlockStateGenerator;
import thetadev.aeriautil.registry.ICustomBlockState;
import thetadev.aeriautil.registry.ICustomLoot;
import thetadev.aeriautil.util.Helper;

import java.util.HashMap;

public class BlockFirefly extends BlockImpl implements ICustomBlockState, ICustomLoot
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = makeCuboidShape(6.2, 5.9, 4.25, 9.8, 9.5, 12.25);
	private static final HashMap<Direction, VoxelShape> SHAPES = new HashMap<>();

	static {
		for(Direction dir : Direction.Plane.HORIZONTAL) {
			SHAPES.put(dir, Helper.rotateShape(Direction.NORTH, dir, SHAPE));
		}
	}

	public BlockFirefly() {
		super("firefly", Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.2F).lightValue(14));
		setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getRaytraceShape(state, worldIn, pos);
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPES.get(state.get(FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public void generateCustomBlockState(BlockStateGenerator generator) {
		generator.horizontalBlock(this, generator.models().getExistingFile(generator.modLoc(getBaseName())));
	}

	@Override
	public LootTable.Builder genLoot() {
		return BlockLootProvider.LootTableHooks.genEmpty();
	}
}
