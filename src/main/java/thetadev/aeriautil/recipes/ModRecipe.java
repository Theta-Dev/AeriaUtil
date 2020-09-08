package thetadev.aeriautil.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class ModRecipe implements IRecipe<RecipeWrapper>
{
	public final ResourceLocation name;

	public ModRecipe(ResourceLocation name) {
		this.name = name;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ResourceLocation getId() {
		return this.name;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World worldIn) {
		return true;
	}

	@Override
	public ItemStack getCraftingResult(RecipeWrapper inv) {
		return getRecipeOutput();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	public abstract void serialize(JsonObject json);
}
