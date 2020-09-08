package thetadev.aeriautil.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import thetadev.aeriautil.recipes.ModRecipe;

import javax.annotation.Nullable;

public class FinishedRecipe implements IFinishedRecipe
{
	private final ModRecipe recipe;

	public FinishedRecipe(ModRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void serialize(JsonObject json) {
		recipe.serialize(json);
	}

	@Override
	public ResourceLocation getID() {
		return recipe.name;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}

	@Nullable
	@Override
	public JsonObject getAdvancementJson() {
		return null;
	}

	@Nullable
	@Override
	public ResourceLocation getAdvancementID() {
		return null;
	}
}
