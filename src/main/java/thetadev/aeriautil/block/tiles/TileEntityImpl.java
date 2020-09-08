package thetadev.aeriautil.block.tiles;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TileEntityImpl extends TileEntity
{
	public TileEntityImpl(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		this.writeNBT(compound, SaveType.TILE);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		this.readNBT(compound, SaveType.TILE);
	}

	public void writeNBT(CompoundNBT compound, SaveType type) {
		if (type != SaveType.BLOCK) {
			super.write(compound);
		}
	}

	public void readNBT(CompoundNBT compound, SaveType type) {
		if (type != SaveType.BLOCK) {
			super.read(compound);
		}
	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT compound = new CompoundNBT();
		this.writeNBT(compound, SaveType.SYNC);
		return new SUpdateTileEntityPacket(this.pos, 0, compound);
	}

	@Override
	public final CompoundNBT getUpdateTag() {
		CompoundNBT compound = new CompoundNBT();
		this.writeNBT(compound, SaveType.SYNC);
		return compound;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.readNBT(tag, SaveType.SYNC);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		this.readNBT(packet.getNbtCompound(), SaveType.SYNC);
	}

	public void sendToClients() {
		ServerWorld world = (ServerWorld) this.getWorld();
		Stream<ServerPlayerEntity> entities = world.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(this.getPos()), false);
		SUpdateTileEntityPacket packet = this.getUpdatePacket();
		entities.forEach(e -> e.connection.sendPacket(packet));
	}

	public IItemHandlerModifiable getItemHandler(Direction facing) {
		return null;
	}

	public List<IItemHandlerModifiable> getItemHandlers() {
		return Arrays.asList(getItemHandler(null));
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler handler = this.getItemHandler(facing);
			return handler == null ? LazyOptional.empty() : LazyOptional.of(() -> (T) handler);
		} else {
			return super.getCapability(capability, facing);
		}
	}

	public void dropInventory() {
		for(IItemHandler handler : getItemHandlers()) {
			if (handler != null) {
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack stack = handler.getStackInSlot(i);
					if (!stack.isEmpty()) {
						ItemEntity item = new ItemEntity(this.world,
								this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
								stack);
						this.world.addEntity(item);
					}
				}
			}
		}
	}

	public void modifyDrop(ItemStack regularItem) {
		CompoundNBT compound = new CompoundNBT();
		this.writeNBT(compound, SaveType.BLOCK);
		if (!compound.isEmpty()) {
			if (!regularItem.hasTag())
				regularItem.setTag(new CompoundNBT());
			regularItem.getTag().put("data", compound);
		}
	}

	public void loadDataOnPlace(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundNBT compound = stack.getTag().getCompound("data");
			if (compound != null)
				this.readNBT(compound, SaveType.BLOCK);
		}
	}

	public enum SaveType {
		TILE,
		SYNC,
		BLOCK
	}
}
