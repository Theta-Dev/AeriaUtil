package thetadev.aeriautil.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import thetadev.aeriautil.block.tiles.TileEntityImpl;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;
import thetadev.aeriautil.registry.ModTileType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BlockContainerImpl extends ContainerBlock implements IModItem
{
	private final String baseName;
	private final ModTileType<? extends TileEntity> tileType;

	public BlockContainerImpl(String baseName, Supplier<TileEntity> tileSupplier, Block.Properties properties) {
		super(properties);

		this.baseName = baseName;
		this.tileType = new ModTileType<>(tileSupplier, this);

		ModRegistry.add(this);
		ModRegistry.add(this.tileType);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return this.tileType.type.create();
	}

	@Override
	public String getBaseName() {
		return this.baseName;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);

		TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
		if (tile instanceof TileEntityImpl) {
			for (ItemStack stack : drops) {
				if (stack.getItem() != this.asItem())
					continue;
				((TileEntityImpl) tile).modifyDrop(stack);
				break;
			}
		}
		return drops;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityImpl)
				((TileEntityImpl) tile).dropInventory();
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityImpl)
			((TileEntityImpl) tile).loadDataOnPlace(stack);
	}

	@Override
	public int tickRate(IWorldReader worldIn) {
		return 4;
	}
}
