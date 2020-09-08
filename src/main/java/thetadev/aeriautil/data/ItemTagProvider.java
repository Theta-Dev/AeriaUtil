package thetadev.aeriautil.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.data.tags.ModItemTags;
import thetadev.aeriautil.item.ModItems;

import javax.annotation.Nonnull;

public class ItemTagProvider extends ItemTagsProvider
{
	public ItemTagProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		this.getBuilder(ModItemTags.COMPOSTABLE)
				.add(ItemTags.LEAVES, ItemTags.SAPLINGS, ItemTags.FLOWERS, Tags.Items.SEEDS, Tags.Items.CROPS, Tags.Items.MUSHROOMS)
				.add(Items.KELP, Items.DRIED_KELP, Items.DRIED_KELP_BLOCK, Items.SEAGRASS, Items.SWEET_BERRIES, Items.CACTUS,
						Items.MELON, Items.MELON_SLICE, Items.SUGAR_CANE, Items.VINE, Items.NETHER_WART, Items.NETHER_WART_BLOCK,
						Items.APPLE, Items.COCOA_BEANS, Items.GRASS, Items.TALL_GRASS, Items.FERN, Items.LARGE_FERN, Items.LILY_PAD,
						Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK, Items.MUSHROOM_STEW, Items.PUMPKIN, Items.CARVED_PUMPKIN,
						Items.SEA_PICKLE, Items.HAY_BLOCK, Items.COOKIE, Items.BREAD, Items.BAKED_POTATO, Items.POISONOUS_POTATO,
						Items.CAKE, Items.PUMPKIN_PIE, Items.ROTTEN_FLESH);

		this.getBuilder(ModItemTags.DIRT).add(Items.DIRT, Items.COARSE_DIRT, Items.GRASS_BLOCK, Items.PODZOL, Items.MYCELIUM);

		this.getBuilder(ModItemTags.HAMMERS).add(ModItems.WOODEN_HAMMER, ModItems.STONE_HAMMER, ModItems.IRON_HAMMER, ModItems.DIAMOND_HAMMER);

		this.getBuilder(Tags.Items.SHEARS).add(ModItems.STONE_SHEARS);
	}

	@Nonnull
	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " Item tags";
	}
}
