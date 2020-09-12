package thetadev.aeriautil.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import thetadev.aeriautil.AeriaUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class LootModifierStick extends LootModifier
{
	protected LootModifierStick(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		BlockState blockState = context.get(LootParameters.BLOCK_STATE);
		if(blockState == null || !blockState.getBlock().getTags().contains(BlockTags.LEAVES.getId())) return generatedLoot;

		LootTable lootTable = context.getLootTable(AeriaUtil.loc("stick_leaves"));
		LootContext newContext = new LootContext.Builder(context.getWorld()).withRandom(context.getRandom()).build(LootParameterSets.EMPTY);

		generatedLoot.addAll(lootTable.generate(newContext));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<LootModifierStick>
	{
		public Serializer() {
			setRegistryName("stick");
		}

		@Override
		public LootModifierStick read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			return new LootModifierStick(ailootcondition);
		}
	}
}
