package thetadev.aeriautil.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.IForgeRegistry;
import thetadev.aeriautil.AeriaUtil;

public class ModRecipes
{
	public static final IRecipeType<SieveRecipe> SIEVE_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<SieveRecipe> SIEVE_SERIALIZER = new SieveRecipe.Serializer();

	public static final IRecipeType<HammerRecipe> HAMMER_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<HammerRecipe> HAMMER_SERIALIZER = new HammerRecipe.Serializer();

	public static final IRecipeType<WashingRecipe> WASHING_TYPE = new RecipeType<>();
	public static final IRecipeSerializer<WashingRecipe> WASHING_SERIALIZER = new WashingRecipe.Serializer();


	public static void register(IForgeRegistry<IRecipeSerializer<?>> registry) {
		register(registry, "sieve", SIEVE_TYPE, SIEVE_SERIALIZER);
		register(registry, "hammer", HAMMER_TYPE, HAMMER_SERIALIZER);
		register(registry, "washing", WASHING_TYPE, WASHING_SERIALIZER);
	}

	private static void register(IForgeRegistry<IRecipeSerializer<?>> registry, String name, IRecipeType<?> type, IRecipeSerializer<?> serializer) {
		ResourceLocation res = AeriaUtil.loc(name);
		Registry.register(Registry.RECIPE_TYPE, res, type);
		registry.register(serializer.setRegistryName(res));
	}

	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T>
	{
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
