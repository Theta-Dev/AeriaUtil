package thetadev.aeriautil.util;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

public class StateIngredientBlock implements StateIngredient {
	private final Block block;

	public StateIngredientBlock(Block block) {
		this.block = block;
	}

	@Override
	public boolean test(BlockState blockState) {
		return block == blockState.getBlock();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "block");
		object.addProperty("block", block.getRegistryName().toString());
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(1);
		buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
	}

	@Override
	public List<BlockState> getDisplayed() {
		return Collections.singletonList(block.getDefaultState());
	}

	@Override
	public String getName() {
		return block.getRegistryName().getPath();
	}

}
