package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thetadev.aeriautil.block.tiles.TileEntityComposter;
import thetadev.aeriautil.data.BlockStateGenerator;
import thetadev.aeriautil.registry.ICustomBlockState;
import thetadev.aeriautil.util.Helper;

public class BlockComposter extends BlockContainerImpl implements ICustomBlockState
{
	public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_8;

	private static final VoxelShape SHAPE_FULL_CUBE = VoxelShapes.fullCube();
	private static final VoxelShape[] SHAPES_COMPOSTER = Util.make(new VoxelShape[9], (shapes) -> {
		for(int i = 0; i < 8; ++i) {
			shapes[i] = VoxelShapes.combineAndSimplify(SHAPE_FULL_CUBE, Block.makeCuboidShape(2.0D, (double)Math.max(2, 1 + i * 2), 2.0D, 14.0D, 16.0D, 14.0D), IBooleanFunction.ONLY_FIRST);
		}

		shapes[8] = shapes[7];
	});

	public BlockComposter() {
		super("composter", TileEntityComposter::new, Block.Properties.from(Blocks.COMPOSTER));
		this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES_COMPOSTER[state.get(LEVEL)];
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE_FULL_CUBE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES_COMPOSTER[0];
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return blockState.get(LEVEL);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
	}

	@Override
	public void generateCustomBlockState(BlockStateGenerator generator) { }

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tile = player.world.getTileEntity(pos);
		if(!(tile instanceof TileEntityComposter)) return ActionResultType.FAIL;

		TileEntityComposter composter = (TileEntityComposter)tile;

		if(composter.getState() == TileEntityComposter.ComposterState.ACCEPTING) return Helper.putStackOnTile(player, handIn, pos, 0, true);
		else return composter.outputItems.drop(true) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
	}
}
