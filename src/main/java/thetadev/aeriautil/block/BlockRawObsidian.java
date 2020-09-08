package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.storage.loot.LootTable;
import thetadev.aeriautil.data.BlockLootProvider;
import thetadev.aeriautil.registry.ICustomLoot;

public class BlockRawObsidian extends BlockImpl implements ICustomLoot
{
	public BlockRawObsidian() {
		super("raw_obsidian", Block.Properties.from(Blocks.OBSIDIAN));
	}

	@Override
	public LootTable.Builder genLoot() {
		return BlockLootProvider.LootTableHooks.genRegular(Blocks.OBSIDIAN);
	}
}
