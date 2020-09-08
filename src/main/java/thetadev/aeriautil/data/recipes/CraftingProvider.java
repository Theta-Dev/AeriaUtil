package thetadev.aeriautil.data.recipes;

import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.ModBlocks;
import thetadev.aeriautil.item.ModItems;

import java.util.function.Consumer;

public class CraftingProvider extends RecipeProvider
{
	public CraftingProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		// HAMMERS
		hammerRecipe(Inp.fromTag(ItemTags.PLANKS), ModItems.WOODEN_HAMMER, consumer);
		hammerRecipe(Inp.fromTag(Tags.Items.COBBLESTONE), ModItems.STONE_HAMMER, consumer);
		hammerRecipe(Inp.fromTag(Tags.Items.INGOTS_IRON), ModItems.IRON_HAMMER, consumer);
		hammerRecipe(Inp.fromTag(Tags.Items.GEMS_DIAMOND), ModItems.DIAMOND_HAMMER, consumer);

		compact3x3(Inp.fromTag(ItemTags.LEAVES), ModBlocks.LEAF_PILE, consumer);
		compact2x2(Inp.fromItem(ModItems.PEBBLE), Items.COBBLESTONE, consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.COMPOSTER)
				.key('#', ItemTags.PLANKS)
				.key('_', ItemTags.WOODEN_SLABS)
				.key('S', ItemTags.SAPLINGS)
				.patternLine("# #")
				.patternLine("#S#")
				.patternLine("#_#")
				.addCriterion("has_item", hasItem(ItemTags.PLANKS))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.SIEVE)
				.key('#', ItemTags.PLANKS)
				.key('I', Tags.Items.RODS_WOODEN)
				.key('S', Tags.Items.STRING)
				.patternLine("SSS")
				.patternLine("# #")
				.patternLine("I I")
				.addCriterion("has_item", hasItem(Tags.Items.STRING))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.ENDER_TOTEM)
				.key('X', Items.TOTEM_OF_UNDYING)
				.key('O', Items.ENDER_EYE)
				.patternLine("OOO")
				.patternLine("OXO")
				.patternLine("OOO")
				.addCriterion("has_item", hasItem(Items.TOTEM_OF_UNDYING))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.UNFIRED_CRUCIBLE)
				.key('#', Items.CLAY_BALL)
				.patternLine("# #")
				.patternLine("# #")
				.patternLine(" # ")
				.addCriterion("has_item", hasItem(Items.CLAY_BALL))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.COBBLE_CRUCIBLE)
				.key('#', ModItems.CRUCIBLE)
				.key('S', Tags.Items.COBBLESTONE)
				.patternLine(" S ")
				.patternLine("S#S")
				.patternLine(" S ")
				.addCriterion("has_item", hasItem(ModItems.CRUCIBLE))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.STONE_SHEARS)
				.key('#', Tags.Items.COBBLESTONE)
				.key('S', Tags.Items.STRING)
				.key('I', Tags.Items.RODS_WOODEN)
				.patternLine("# #")
				.patternLine(" S ")
				.patternLine("I I")
				.addCriterion("has_item", hasItem(Tags.Items.COBBLESTONE))
				.build(consumer);
	}

	private void specialRecipe(Consumer<IFinishedRecipe> consumer, SpecialRecipeSerializer<?> serializer) {
		ResourceLocation name = ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer);
		CustomRecipeBuilder.customRecipe(serializer).build(consumer, AeriaUtil.loc("dynamic/" + name.getPath()).toString());
	}

	private void hammerRecipe(Inp material, IItemProvider result, Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(result)
				.key('X', material.ingredient)
				.key('I', Tags.Items.RODS_WOODEN)
				.patternLine(" X ")
				.patternLine(" IX")
				.patternLine("I  ")
				.addCriterion("has_item", hasItem(material.predicate))
				.build(consumer);
	}

	private void compact3x3(Inp material, IItemProvider result, Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(result)
				.key('X', material.ingredient)
				.patternLine("XXX")
				.patternLine("XXX")
				.patternLine("XXX")
				.addCriterion("has_item", hasItem(material.predicate))
				.build(consumer, AeriaUtil.loc(result.asItem().getRegistryName().getPath()));
	}

	private void compact2x2(Inp material, IItemProvider result, Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(result)
				.key('X', material.ingredient)
				.patternLine("XX")
				.patternLine("XX")
				.addCriterion("has_item", hasItem(material.predicate))
				.build(consumer, AeriaUtil.loc(result.asItem().getRegistryName().getPath()));
	}

	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " crafting recipes";
	}
}
