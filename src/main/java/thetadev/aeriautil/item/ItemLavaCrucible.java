package thetadev.aeriautil.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemLavaCrucible extends ItemImpl
{
	public ItemLavaCrucible() {
		super("lava_crucible", new Properties().maxDamage(200));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if(ret != null) return ret;
		if(raytraceresult.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.resultPass(itemstack);
		}
		else if(raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.resultPass(itemstack);
		}
		else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getPos();
			Direction direction = blockraytraceresult.getFace();
			BlockPos blockpos1 = blockpos.offset(direction);

			BlockState blockstate = worldIn.getBlockState(blockpos);
			BlockPos blockpos2 = canBlockContainFluid(worldIn, blockpos, blockstate) ? blockpos : blockpos1;
			if(this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos2, itemstack, blockraytraceresult)) {
				this.onLiquidPlaced(worldIn, itemstack, blockpos2);
				if(playerIn instanceof ServerPlayerEntity) {
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockpos2, itemstack);
				}

				playerIn.addStat(Stats.ITEM_USED.get(this));
				return ActionResult.resultConsume(itemstack);
			}
			else {
				return ActionResult.resultFail(itemstack);
			}
		}
	}

	public boolean tryPlaceContainedLiquid(PlayerEntity player, World worldIn, BlockPos posIn, ItemStack itemstack, @Nullable BlockRayTraceResult p_180616_4_) {
		if(!worldIn.isBlockModifiable(player, posIn) && player.canPlayerEdit(posIn, p_180616_4_.getFace(), itemstack)) return false;

		BlockState blockstate = worldIn.getBlockState(posIn);
		Block block = blockstate.getBlock();
		Material material = blockstate.getMaterial();
		boolean flag = blockstate.isReplaceable(Fluids.LAVA);
		boolean flag1 = blockstate.isAir() || flag || block instanceof ILiquidContainer && ((ILiquidContainer) block).canContainFluid(worldIn, posIn, blockstate, Fluids.LAVA);
		if(!flag1) {
			return p_180616_4_ != null && this.tryPlaceContainedLiquid(player, worldIn, p_180616_4_.getPos().offset(p_180616_4_.getFace()), itemstack, (BlockRayTraceResult) null);
		}
		else if(block instanceof ILiquidContainer && ((ILiquidContainer) block).canContainFluid(worldIn, posIn, blockstate, Fluids.LAVA)) {
			((ILiquidContainer) block).receiveFluid(worldIn, posIn, blockstate, (Fluids.LAVA).getStillFluidState(false));
			this.playEmptySound(player, worldIn, posIn);
			return true;
		}
		else {
			if(!worldIn.isRemote && flag && !material.isLiquid()) {
				worldIn.destroyBlock(posIn, true);
			}

			if(!worldIn.setBlockState(posIn, Blocks.LAVA.getDefaultState().getBlockState(), 11) && !blockstate.getFluidState().isSource()) {
				return false;
			}
			else {
				this.playEmptySound(player, worldIn, posIn);
				return true;
			}
		}
	}

	protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
		worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate) {
		return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, posIn, blockstate, Fluids.LAVA);
	}

	public void onLiquidPlaced(World world, ItemStack stack, BlockPos pos) {
		stack.shrink(1);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!(entity instanceof PlayerEntity) || world.isRemote) return;
		PlayerEntity player = (PlayerEntity) entity;
		int uses = stack.getMaxDamage() - stack.getDamage();

		if(uses > 1) stack.damageItem(1, player, (e) -> e.sendBreakAnimation(player.swingingHand));
		else if(uses == 1) {
			if(tryPlaceContainedLiquid(player, world, new BlockPos(player.getPositionVec()), stack, null)) {
				stack.damageItem(1, player, (e) -> e.sendBreakAnimation(player.swingingHand));
			}
		}
	}

	@Override
	public boolean hasTooltip() {
		return true;
	}
}
