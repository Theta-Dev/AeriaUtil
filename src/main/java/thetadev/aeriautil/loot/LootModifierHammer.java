package thetadev.aeriautil.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import thetadev.aeriautil.recipes.HammerRecipe;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class LootModifierHammer extends LootModifier
{
	protected LootModifierHammer(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack output = HammerRecipe.getOutput(context.get(LootParameters.BLOCK_STATE), context.getWorld());
		if(output.isEmpty()) return generatedLoot;
		return Collections.singletonList(output);
	}

	public static class Serializer extends GlobalLootModifierSerializer<LootModifierHammer>
	{
		public Serializer() {
			setRegistryName("hammer");
		}

		@Override
		public LootModifierHammer read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			return new LootModifierHammer(ailootcondition);
		}
	}
}
