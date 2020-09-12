package thetadev.aeriautil.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.data.tags.ModItemTags;
import thetadev.aeriautil.item.ModItems;
import thetadev.aeriautil.recipes.WashingRecipe;

import java.util.function.Consumer;

public class WashingProvider extends RecipeProvider
{
	public WashingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		washing(Inp.fromItem(Items.GRAVEL), new ItemStack(Items.IRON_NUGGET), 1, 0.5F, consumer);

		washing(Inp.fromItem(Items.RED_SAND), new ItemStack(Items.GOLD_NUGGET), 1, 0.25F, consumer);
		washing(Inp.fromItem(Items.RED_SAND), new ItemStack(Items.REDSTONE), 1, 0.25F, consumer);
	}

	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " washing recipes";
	}

	private static ResourceLocation idFor(String a, String b) {
		return AeriaUtil.loc("washing/" + a + "_to_" + b);
	}

	private void washing(Inp input, ItemStack output, int numRolls, float chance, Consumer<IFinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(new WashingRecipe(idFor(input.name, output.getItem().getRegistryName().getPath()), input.ingredient, output, numRolls, chance)));
	}
}