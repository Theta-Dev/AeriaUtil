package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks
{
	public static Block COMPOSTER;
	public static Block SIEVE;
	public static Block DUST;
	public static Block LEAF_PILE;
	public static Block RAW_OBSIDIAN;
	public static Block FIREFLY;


	public static void register(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
				new BlockComposter(),
				new BlockSieve(),
				new BlockDust(),
				new BlockLeafPile(),
				new BlockRawObsidian(),
				new BlockFirefly()
		);
	}
}
