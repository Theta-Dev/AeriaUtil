package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

public class BlockDust extends ConcretePowderBlock implements IModItem
{
	public BlockDust() {
		super(Blocks.CLAY, Block.Properties.from(Blocks.SAND));
		ModRegistry.add(this);
	}

	@Override
	public String getBaseName() {
		return "dust";
	}
}
