package thetadev.aeriautil.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.data.tags.ModItemTags;
import thetadev.aeriautil.item.ModItems;
import thetadev.aeriautil.recipes.SieveRecipe;

import java.util.function.Consumer;

public class SieveProvider extends RecipeProvider
{
	public SieveProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(ModItems.PEBBLE), 8, 1, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.WHEAT_SEEDS), 3, 0.05F, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.BEETROOT_SEEDS), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.MELON_SEEDS), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.PUMPKIN_SEEDS), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.CARROT), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ModItemTags.DIRT), new ItemStack(Items.POTATO), 1, 0.01F, consumer);

		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.OAK_SAPLING), 1, 0.2F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.SPRUCE_SAPLING), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.ACACIA_SAPLING), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.BIRCH_SAPLING), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.DARK_OAK_SAPLING), 1, 0.01F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.JUNGLE_SAPLING), 1, 0.005F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.SWEET_BERRIES), 1, 0.005F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.APPLE), 1, 0.05F, consumer);
		sieve(Inp.fromTag(ItemTags.LEAVES), new ItemStack(Items.STICK), 1, 0.1F, consumer);
	}

	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " sieve recipes";
	}

	private static ResourceLocation idFor(String a, String b) {
		return AeriaUtil.loc("sieve/" + a + "_to_" + b);
	}

	private void sieve(Inp input, ItemStack output, int maxCount, float chance, Consumer<IFinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(new SieveRecipe(idFor(input.name, output.getItem().getRegistryName().getPath()), input.ingredient, output, maxCount, chance)));
	}
}
