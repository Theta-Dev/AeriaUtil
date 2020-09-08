package thetadev.aeriautil.util;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;

import java.util.List;
import java.util.function.Predicate;

public interface StateIngredient extends Predicate<BlockState> {

	JsonObject serialize();

	void write(PacketBuffer buffer);

	List<BlockState> getDisplayed();

	String getName();
}