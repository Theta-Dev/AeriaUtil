package thetadev.aeriautil.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

import java.util.Set;

public class ItemHammer extends ToolItem implements IModItem
{
	private final String baseName;

	private static final Set<Block> effectiveBlocksOn = Sets
			.newHashSet(Blocks.STONE, Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.SAND);

	public ItemHammer(String baseName, IItemTier tier) {
		super(0.5F, 0.5F, tier, effectiveBlocksOn, ItemImpl.baseProperties().addToolType(ToolType.PICKAXE, tier.getHarvestLevel()));
		this.baseName = baseName;
		ModRegistry.add(this);
	}

	@Override
	public String getBaseName() {
		return baseName;
	}
}
