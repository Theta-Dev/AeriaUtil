package thetadev.aeriautil.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.registry.ICustomLoot;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockLootProvider implements IDataProvider
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	private final Map<Block, Function<Block, LootTable.Builder>> lootFunctions = new HashMap<>();

	public BlockLootProvider(DataGenerator generator) {
		this.generator = generator;

		for (IModItem item : ModRegistry.ALL_ITEMS) {
			if (!(item instanceof Block)) continue;
			Block block = (Block) item;

			if(item instanceof ICustomLoot) this.lootFunctions.put(block, b -> ((ICustomLoot)item).genLoot());
			else this.lootFunctions.put(block, LootTableHooks::genRegular);
		}
	}

	private static Path getPath(Path root, ResourceLocation res) {
		return root.resolve("data/" + res.getNamespace() + "/loot_tables/blocks/" + res.getPath() + ".json");
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		for (Map.Entry<Block, Function<Block, LootTable.Builder>> function : this.lootFunctions.entrySet()) {
			Block block = function.getKey();
			Function<Block, LootTable.Builder> func = function.getValue();
			LootTable table = func.apply(block).setParameterSet(LootParameterSets.BLOCK).build();
			Path path = getPath(this.generator.getOutputFolder(), block.getRegistryName());
			IDataProvider.save(GSON, cache, LootTableManager.toJson(table), path);
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " Loot";
	}

	public static class LootTableHooks extends BlockLootTables
	{
		public static LootTable.Builder genEmpty() {
			return LootTable.builder();
		}

		public static LootTable.Builder genLeaves(Block block, Block drop) {
			return droppingWithChancesAndSticks(block, drop, 0.05F, 0.0625F, 0.083333336F, 0.1F);
		}

		public static LootTable.Builder genSlab(Block block) {
			return droppingSlab(block);
		}

		public static LootTable.Builder genRegular(Block block) {
			return dropping(block);
		}

		public static LootTable.Builder genSilkOnly(Block block) {
			return onlyWithSilkTouch(block);
		}

		public static LootTable.Builder genSilkOr(Block block, LootEntry.Builder<?> builder) {
			return droppingWithSilkTouch(block, builder);
		}

		public static LootTable.Builder genFlowerPot(Block block) {
			return droppingAndFlowerPot(((FlowerPotBlock) block).func_220276_d());
		}

		public static <T> T survivesExplosion(Block block, ILootConditionConsumer<T> then) {
			return withSurvivesExplosion(block, then);
		}
	}
}