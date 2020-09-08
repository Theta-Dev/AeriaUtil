package thetadev.aeriautil.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thetadev.aeriautil.util.Helper;
import thetadev.aeriautil.util.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SieveRecipe extends ModRecipe
{
	public final Ingredient input;
	public final ItemStack output;
	public final int maxCount;
	public final float chance;

	public SieveRecipe(ResourceLocation name, Ingredient input, ItemStack output, int maxCount, float chance) {
		super(name);
		this.input = input;
		this.output = output;
		this.maxCount = maxCount;
		this.chance = chance;
	}

	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	public ItemStack getOutput(Random rng) {
		if(rng.nextFloat() <= chance) return ItemHandlerHelper.copyStackWithSize(output, Helper.random(rng, output.getCount(), maxCount+1));
		else return ItemStack.EMPTY;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.SIEVE_SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipes.SIEVE_TYPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SieveRecipe>
	{
		@Override
		public SieveRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new SieveRecipe(recipeId, Ingredient.deserialize(json.get("input")),
					CraftingHelper.getItemStack(json.getAsJsonObject("output"), true),
					json.get("maxCount").getAsInt(),
					json.get("chance").getAsFloat());
		}

		@Nullable
		@Override
		public SieveRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new SieveRecipe(recipeId, Ingredient.read(buffer), buffer.readItemStack(), buffer.readInt(), buffer.readFloat());
		}

		@Override
		public void write(PacketBuffer buffer, SieveRecipe recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.maxCount);
			buffer.writeFloat(recipe.chance);
		}
	}

	@Override
	public void serialize(JsonObject json) {
		json.add("input", input.serialize());
		json.add("output", ItemNBTHelper.serializeStack(output));
		json.addProperty("maxCount", maxCount);
		json.addProperty("chance", chance);
	}

	public static List<SieveRecipe> getRecipes(ItemStack input, World world) {
		List<SieveRecipe> recipeList = world.getRecipeManager().getRecipes(ModRecipes.SIEVE_TYPE, null, null);
		return recipeList.stream().filter(recipe -> recipe.matches(input)).collect(Collectors.toList());
	}

	public static List<ItemStack> getResult(ItemStack input, World world) {
		List<SieveRecipe> recipeList = getRecipes(input, world);

		return recipeList.stream().map(recipe -> recipe.getOutput(world.getRandom())).collect(Collectors.toList());
	}
}
