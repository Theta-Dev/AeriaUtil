package thetadev.aeriautil.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ItemEnderTotem extends ItemImpl
{
	public ItemEnderTotem() {
		super("ender_totem", new Properties().maxStackSize(1));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!isSelected || !(entity instanceof PlayerEntity) || world.isRemote) return;
		PlayerEntity player = (PlayerEntity) entity;

		// Teleport player to the end when falling into the void
		if(world.getDimension().getType() != DimensionType.THE_END && player.serverPosY < -100 && player.fallDistance > 10) {
			player.fallDistance = 0;
			player.changeDimension(DimensionType.THE_END);
		}
	}

	@Override
	public boolean hasTooltip() {
		return true;
	}
}
