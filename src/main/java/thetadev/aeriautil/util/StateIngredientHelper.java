package thetadev.aeriautil.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class StateIngredientHelper {
	public static StateIngredient of(Block block) {
		return new StateIngredientBlock(block);
	}

	public static StateIngredient of(Tag<Block> tag) {
		return new StateIngredientTag(tag);
	}

	public static StateIngredient of(ResourceLocation id) {
		return new StateIngredientTag(id);
	}

	public static StateIngredient deserialize(JsonObject object) {
		switch (JSONUtils.getString(object, "type")) {
			case "tag":
				return new StateIngredientTag(new BlockTags.Wrapper(new ResourceLocation(JSONUtils.getString(object, "tag"))));
			case "block":
				return new StateIngredientBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(object, "block"))));
			default:
				throw new JsonParseException("Unknown type!");
		}
	}

	public static StateIngredient read(PacketBuffer buffer) {
		switch (buffer.readVarInt()) {
			case 0:
				return new StateIngredientTag(buffer.readResourceLocation());
			case 1:
				return new StateIngredientBlock(buffer.readRegistryIdUnsafe(ForgeRegistries.BLOCKS));
			default:
				throw new IllegalArgumentException("Unknown input discriminator!");
		}
	}

	/**
	 * Writes data about the block state to the provided json object.
	 */
	public static JsonObject serializeBlockState(BlockState state) {
		CompoundNBT nbt = NBTUtil.writeBlockState(state);
		ItemNBTHelper.renameTag(nbt, "Name", "name");
		ItemNBTHelper.renameTag(nbt, "Properties", "properties");
		Dynamic<INBT> dyn = new Dynamic<>(NBTDynamicOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	/**
	 * Reads the block state from the provided json object.
	 */
	public static BlockState readBlockState(JsonObject object) {
		CompoundNBT nbt = (CompoundNBT) new Dynamic<>(JsonOps.INSTANCE, object).convert(NBTDynamicOps.INSTANCE).getValue();
		ItemNBTHelper.renameTag(nbt, "name", "Name");
		ItemNBTHelper.renameTag(nbt, "properties", "Properties");
		String name = nbt.getString("Name");
		ResourceLocation id = ResourceLocation.tryCreate(name);
		if (id == null || !ForgeRegistries.BLOCKS.containsKey(id)) {
			throw new IllegalArgumentException("Invalid or unknown block ID: " + name);
		}
		return NBTUtil.readBlockState(nbt);
	}
}
