package thetadev.aeriautil.util;

import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.tiles.TileEntityImpl;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Helper
{
	public static ActionResultType putStackOnTile(PlayerEntity player, Hand hand, BlockPos pos, int slot, boolean sound) {
		TileEntity tile = player.world.getTileEntity(pos);
		if(tile instanceof TileEntityImpl) {
			IItemHandlerModifiable handler = ((TileEntityImpl) tile).getItemHandler(null);
			if(handler != null) {
				ItemStack handStack = player.getHeldItem(hand);
				if(!handStack.isEmpty()) {
					ItemStack remain = handler.insertItem(slot, handStack, player.world.isRemote);
					if(!ItemStack.areItemStacksEqual(remain, handStack)) {
						if(sound)
							player.world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
									SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.PLAYERS, 0.75F, 1F);
						if(!player.world.isRemote)
							player.setHeldItem(hand, remain);
						return ActionResultType.SUCCESS;
					}
				}

				if(!handler.getStackInSlot(slot).isEmpty()) {
					if(sound)
						player.world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
								SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.PLAYERS, 0.75F, 1F);
					if(!player.world.isRemote) {
						ItemStack stack = handler.getStackInSlot(slot);
						if(!player.addItemStackToInventory(stack)) {
							ItemEntity item = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), stack);
							player.world.addEntity(item);
						}
						handler.setStackInSlot(slot, ItemStack.EMPTY);
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.FAIL;
	}

	public static double map(double input, double minA, double maxA, double minB, double maxB) {
		return ((maxB - minB) * (input / (maxA - minA))) + minB;
	}

	public static boolean dropItems(List<ItemStack> items, World world, double x, double y, double z) {
		boolean res = false;

		for(ItemStack stack : items) {
			ItemEntity item = new ItemEntity(world, x, y, z, stack);
			world.addEntity(item);
			res = true;
		}
		return res;
	}

	public static int random(Random rng, int min, int max) {
		if(min == max) return min;
		if(min < 0 || max < 0) return 0;

		int nmin = Math.min(min, max);
		int nmax = Math.max(min, max);
		return rng.nextInt(nmax - nmin) + nmin;
	}

	public static ResourceLocation blockTextureLoc(String baseName, String suffix) {
		if(suffix.isEmpty()) return AeriaUtil.loc("block/"+baseName);
		return AeriaUtil.loc("block/" + baseName + "_" + suffix);
	}

	public static void spawnBlockParticles(World world, BlockState blockState, int n, double x, double y, double z, double spreadx, double spready, double spreadz) {
		Function<Pair<Double, Double>, Double> getspread = v -> {
			Random rng = world.getRandom();
			return v.getKey() + v.getValue()*rng.nextDouble()*(rng.nextBoolean() ? 1:-1);
		};

		for(int i=0; i<n; i++) {
			world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState), getspread.apply(new Pair<>(x, spreadx)), getspread.apply(new Pair<>(y, spready)), getspread.apply(new Pair<>(z, spreadz)), 0, 0, 0);
		}
	}
}
