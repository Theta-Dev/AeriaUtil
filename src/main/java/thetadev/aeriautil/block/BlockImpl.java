package thetadev.aeriautil.block;

import net.minecraft.block.Block;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

public class BlockImpl extends Block implements IModItem
{
	private final String baseName;

	public BlockImpl(String baseName, Block.Properties properties) {
		super(properties);
		this.baseName = baseName;
		ModRegistry.add(this);
	}

	@Override
	public String getBaseName() {
		return this.baseName;
	}
}
