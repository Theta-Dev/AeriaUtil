package thetadev.aeriautil.item;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

public class ItemStoneShears extends ShearsItem implements IModItem
{
	public ItemStoneShears() {
		super(ItemImpl.baseProperties().maxDamage(60));
		registerDispenseBehavior();
		ModRegistry.add(this);
	}

	private void registerDispenseBehavior() {
		DispenserBlock.registerDispenseBehavior(this, new OptionalDispenseBehavior()
		{
			/**
			 * Dispense the specified stack, play the dispense sound and spawn particles.
			 */
			protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
				World world = source.getWorld();
				if(!world.isRemote()) {
					this.successful = false;
					BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));

					for(net.minecraft.entity.Entity entity : world.getEntitiesInAABBexcluding((net.minecraft.entity.Entity) null, new AxisAlignedBB(blockpos), e -> !e.isSpectator() && e instanceof net.minecraftforge.common.IShearable)) {
						net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) entity;
						if(target.isShearable(stack, world, blockpos)) {
							java.util.List<ItemStack> drops = target.onSheared(stack, entity.world, blockpos,
									net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantments.FORTUNE, stack));
							java.util.Random rand = new java.util.Random();
							drops.forEach(d -> {
								net.minecraft.entity.item.ItemEntity ent = entity.entityDropItem(d, 1.0F);
								ent.setMotion(ent.getMotion().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
							});
							if(stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
								stack.setCount(0);
							}

							this.successful = true;
							break;
						}
					}

					if(!this.successful) {
						BlockState blockstate = world.getBlockState(blockpos);
						if(blockstate.isIn(BlockTags.BEEHIVES)) {
							int i = blockstate.get(BeehiveBlock.HONEY_LEVEL);
							if(i >= 5) {
								if(stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
									stack.setCount(0);
								}

								BeehiveBlock.dropHoneyComb(world, blockpos);
								((BeehiveBlock) blockstate.getBlock()).takeHoney(world, blockstate, blockpos, (PlayerEntity) null, BeehiveTileEntity.State.BEE_RELEASED);
								this.successful = true;
							}
						}
					}
				}

				return stack;
			}
		});
	}

	@Override
	public String getBaseName() {
		return "stone_shears";
	}
}
