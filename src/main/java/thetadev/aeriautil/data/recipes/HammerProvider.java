package thetadev.aeriautil.data.recipes;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.ModBlocks;
import thetadev.aeriautil.recipes.HammerRecipe;
import thetadev.aeriautil.util.StateIngredient;
import thetadev.aeriautil.util.StateIngredientBlock;

import java.util.function.Consumer;

public class HammerProvider extends RecipeProvider
{
	public HammerProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		hammer(new StateIngredientBlock(Blocks.COBBLESTONE), new ItemStack(Items.GRAVEL), consumer);
		hammer(new StateIngredientBlock(Blocks.GRAVEL), new ItemStack(Items.SAND), consumer);
		hammer(new StateIngredientBlock(Blocks.SAND), new ItemStack(ModBlocks.DUST), consumer);
	}

	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " hammer recipes";
	}

	private static ResourceLocation idFor(String a, String b) {
		return AeriaUtil.loc("hammer/" + a + "_to_" + b);
	}

	private void hammer(StateIngredient input, ItemStack output, Consumer<IFinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(new HammerRecipe(idFor(input.getName(), output.getItem().getRegistryName().getPath()), input, output)));
	}
}
