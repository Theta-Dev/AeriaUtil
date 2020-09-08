package thetadev.aeriautil.block.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.items.IItemHandlerModifiable;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.BlockComposter;
import thetadev.aeriautil.data.tags.ModItemTags;

import java.util.Arrays;
import java.util.List;

public class TileEntityComposter extends TileEntityImpl implements ITickableTileEntity
{
	private static final int MAX_LEVEL = 7;
	private static final int PROCESSING_TIME = 100;
	private static final ResourceLocation LOOT_TABLE = AeriaUtil.loc("composter");

	public final ItemStackHandlerTE inputItems = new ItemStackHandlerTE(1, this, false) {
		@Override
		public int getSlotLimit(int slot) {return 1;}

		@Override
		protected boolean canInsert(ItemStack stack, int slot) {
			return getState() == ComposterState.ACCEPTING && stack.getItem().getTags().contains(ModItemTags.COMPOSTABLE.getId());
		}

		@Override
		protected boolean canExtract(ItemStack stack, int slot, int amount) {
			return false;
		}

		@Override
		protected void onContentsChanged(int slot) {
			processInput();
			super.onContentsChanged(slot);
		}
	};

	public final ItemStackHandlerTE outputItems = new ItemStackHandlerTE(0, this, true) {
		@Override
		protected boolean canInsert(ItemStack stack, int slot) {
			return false;
		}

		@Override
		protected boolean canExtract(ItemStack stack, int slot, int amount) {
			return getState() == ComposterState.EXTRACTING;
		}

		@Override
		protected void onContentsChanged(int slot) {
			processOutput();
			super.onContentsChanged(slot);
		}
	};

	public int processingTicks = 0;

	public TileEntityComposter() {
		super(ModTileEntities.COMPOSTER);
	}

	@Override
	public void tick() {
		if(world == null || world.isRemote) return;
		ServerWorld serverWorld = (ServerWorld) world;

		if(getState() == ComposterState.PROCESSING) {
			processingTicks--;

			if(getState() == ComposterState.EXTRACTING) {
				// Generate output
				LootTable lootTable = serverWorld.getServer().getLootTableManager().getLootTableFromLocation(LOOT_TABLE);
				LootContext ctx = new LootContext.Builder(serverWorld)
						.withParameter(LootParameters.POSITION, pos)
						.build(LootParameterSets.COMMAND);
				List<ItemStack> loot = lootTable.generate(ctx);

				outputItems.setSize(loot.size());
				for(int i=0; i<loot.size(); i++) outputItems.setStackInSlot(i, loot.get(i));

				setLevel(8);
			}
		}
	}

	private void processInput() {
		ItemStack input = inputItems.getStackInSlot(0);
		if(input.isEmpty()) return;

		inputItems.setStackInSlot(0, ItemStack.EMPTY);
		int level = Math.min(getLevel()+1, MAX_LEVEL);

		if(level == MAX_LEVEL) {
			processingTicks = PROCESSING_TIME;
		}
		setLevel(level);
	}

	private void processOutput() {
		// Output empty?
		for(int i=0; i<outputItems.getSlots(); i++) {
			if(!outputItems.getStackInSlot(i).isEmpty()) return;
		}

		setLevel(0);
	}

	public int getLevel() {
		return getBlockState().get(BlockComposter.LEVEL);
	}

	public void setLevel(int level) {
		this.world.setBlockState(this.pos, getBlockState().with(BlockComposter.LEVEL, level), 3);
		markDirty();
	}

	public enum ComposterState
	{
		ACCEPTING,
		PROCESSING,
		EXTRACTING
	}

	public ComposterState getState() {
		if(getLevel() < MAX_LEVEL) return ComposterState.ACCEPTING;
		if(processingTicks == 0) return ComposterState.EXTRACTING;
		return ComposterState.PROCESSING;
	}

	@Override
	public void writeNBT(CompoundNBT compound, SaveType type) {
		super.writeNBT(compound, type);
		if(type != SaveType.BLOCK) {
			if(getState() == ComposterState.EXTRACTING) compound.put("outputItems", outputItems.serializeNBT());
			compound.putInt("ticks", processingTicks);
		}
	}

	@Override
	public void readNBT(CompoundNBT compound, SaveType type) {
		super.readNBT(compound, type);
		if(type != SaveType.BLOCK) {
			outputItems.deserializeNBT(compound.getCompound("outputItems"));
			processingTicks = compound.getInt("ticks");
		}
	}

	@Override
	public IItemHandlerModifiable getItemHandler(Direction facing) {
		if(facing == null) return getState() == ComposterState.ACCEPTING ? inputItems : outputItems;
		if(facing == Direction.DOWN) return outputItems;
		return inputItems;
	}

	@Override
	public List<IItemHandlerModifiable> getItemHandlers() {
		return Arrays.asList(outputItems);
	}
}
