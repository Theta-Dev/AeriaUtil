package thetadev.aeriautil.block.tiles;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import thetadev.aeriautil.util.Helper;

import javax.annotation.Nonnull;

public class ItemStackHandlerTE extends ItemStackHandler
{
	private final TileEntityImpl tile;
	private final boolean sendToClients;

	public ItemStackHandlerTE(int size, TileEntityImpl tile, boolean sendToClients) {
		super(size);
		this.tile = tile;
		this.sendToClients = sendToClients;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.tile != null) {
			this.tile.markDirty();
			if (this.sendToClients && !this.tile.getWorld().isRemote)
				this.tile.sendToClients();
		}
	}

	protected boolean canInsert(ItemStack stack, int slot) {
		return true;
	}

	protected boolean canExtract(ItemStack stack, int slot, int amount) {
		return true;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return this.canInsert(stack, slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (this.canInsert(stack, slot)) {
			return super.insertItem(slot, stack, simulate);
		} else {
			return stack;
		}
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.canExtract(this.getStackInSlot(slot), slot, amount)) {
			return super.extractItem(slot, amount, simulate);
		} else {
			return ItemStack.EMPTY;
		}
	}

	public void empty() {
		for(int i=0; i<getSlots(); i++) {
			setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	public boolean drop(boolean above) {
		if(tile.getWorld() == null) return false;
		boolean res = Helper.dropItems(stacks, tile.getWorld(), tile.getPos().getX()+0.5, tile.getPos().getY()+(above ? 1:0.5), tile.getPos().getZ()+0.5);
		empty();
		return res;
	}
}