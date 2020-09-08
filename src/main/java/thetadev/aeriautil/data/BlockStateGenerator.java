package thetadev.aeriautil.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.registry.ICustomBlockState;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.ModRegistry;

import javax.annotation.Nonnull;

public class BlockStateGenerator extends BlockStateProvider
{
	public BlockStateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, AeriaUtil.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (IModItem item : ModRegistry.ALL_ITEMS) {
			if (!(item instanceof Block))
				continue;
			Block block = (Block) item;
			if (block instanceof ICustomBlockState) {
				((ICustomBlockState) block).generateCustomBlockState(this);
			} else {
				this.simpleBlock(block);
			}
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " Blockstates";
	}
}
