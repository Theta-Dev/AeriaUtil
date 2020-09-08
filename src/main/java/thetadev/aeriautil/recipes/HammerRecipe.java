package thetadev.aeriautil.recipes;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import thetadev.aeriautil.util.ItemNBTHelper;
import thetadev.aeriautil.util.StateIngredient;
import thetadev.aeriautil.util.StateIngredientHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class HammerRecipe extends ModRecipe
{
	public final StateIngredient input;
	public final ItemStack output;

	public HammerRecipe(ResourceLocation name, StateIngredient input, ItemStack output) {
		super(name);
		this.input = input;
		this.output = output;
	}

	public boolean matches(BlockState state) {
		return input.test(state);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.HAMMER_SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipes.HAMMER_TYPE;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HammerRecipe>
	{
		@Override
		public HammerRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new HammerRecipe(recipeId,
					StateIngredientHelper.deserialize(json.getAsJsonObject("input")),
					CraftingHelper.getItemStack(json.getAsJsonObject("output"), true));
		}

		@Nullable
		@Override
		public HammerRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new HammerRecipe(recipeId,
					StateIngredientHelper.read(buffer),
					buffer.readItemStack());
		}

		@Override
		public void write(PacketBuffer buffer, HammerRecipe recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
		}
	}

	@Override
	public void serialize(JsonObject json) {
		json.add("input", input.serialize());
		json.add("output", ItemNBTHelper.serializeStack(output));
	}

	public static List<HammerRecipe> getRecipes(BlockState input, World world) {
		List<HammerRecipe> recipeList = world.getRecipeManager().getRecipes(ModRecipes.HAMMER_TYPE, null, null);
		return recipeList.stream().filter(recipe -> recipe.matches(input)).collect(Collectors.toList());
	}

	public static ItemStack getOutput(BlockState input, World world) {
		List<HammerRecipe> recipeList = getRecipes(input, world);
		if(recipeList.isEmpty()) return ItemStack.EMPTY;
		return recipeList.get(0).getRecipeOutput();
	}
}
