package thetadev.aeriautil.data.recipes;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.item.ModItems;
import thetadev.aeriautil.recipes.SieveRecipe;

import java.util.function.Consumer;

public class SmeltingProvider extends RecipeProvider
{
	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		smeltingGeneric(Inp.fromItem(ModItems.UNFIRED_CRUCIBLE), ModItems.CRUCIBLE, consumer);
		smeltingGeneric(Inp.fromItem(ModItems.COBBLE_CRUCIBLE), ModItems.LAVA_CRUCIBLE, consumer);
	}

	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " smelting recipes";
	}

	private static ResourceLocation idFor(String a, String b) {
		return AeriaUtil.loc("smelting/" + a + "_to_" + b);
	}

	private static ResourceLocation idFor(String a, String b, String c) {
		return AeriaUtil.loc("smelting/" + a + "_to_" + b + "_" + c);
	}

	private void smeltingGeneric(Inp input, IItemProvider output, Consumer<IFinishedRecipe> consumer) {
		smelting(input, output, 1, 0.35F, false, false, consumer);
	}

	private void smeltingFood(Inp input, IItemProvider output, Consumer<IFinishedRecipe> consumer) {
		smelting(input, output, 1, 0.35F, false, true, consumer);
	}

	private void smeltingMetal(Inp input, IItemProvider output, Consumer<IFinishedRecipe> consumer) {
		smelting(input, output, 1, 0.35F, true, false, consumer);
	}

	private void smelting(Inp input, IItemProvider output, int count, float xp, boolean blast, boolean campfire, Consumer<IFinishedRecipe> consumer) {
		CookingRecipeBuilder.smeltingRecipe(input.ingredient, output, xp, 200)
				.addCriterion("has_item", hasItem(input.predicate))
				.build(consumer, idFor(input.name, output.asItem().getRegistryName().getPath()));

		if(blast) {
			CookingRecipeBuilder.blastingRecipe(input.ingredient, output, xp, 100)
					.addCriterion("has_item", hasItem(input.predicate))
					.build(consumer, idFor(input.name, output.asItem().getRegistryName().getPath(), "blasting"));
		}
		if(campfire) {
			CookingRecipeBuilder.cookingRecipe(input.ingredient, output, xp, 600, CookingRecipeSerializer.CAMPFIRE_COOKING)
					.addCriterion("has_item", hasItem(input.predicate))
					.build(consumer, idFor(input.name, output.asItem().getRegistryName().getPath(), "cooking"));
		}
	}
}
