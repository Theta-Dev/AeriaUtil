package thetadev.aeriautil.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.event.RegistryEvent;

public class ModItems
{
	public static Item PEBBLE;
	public static Item WOODEN_HAMMER;
	public static Item STONE_HAMMER;
	public static Item IRON_HAMMER;
	public static Item DIAMOND_HAMMER;
	public static Item ENDER_TOTEM;
	public static Item STONE_SHEARS;
	public static Item UNFIRED_CRUCIBLE;
	public static Item CRUCIBLE;
	public static Item COBBLE_CRUCIBLE;
	public static Item LAVA_CRUCIBLE;


	public static void register(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				new ItemImpl("pebble"),
				new ItemHammer("wooden_hammer", ItemTier.WOOD),
				new ItemHammer("stone_hammer", ItemTier.STONE),
				new ItemHammer("iron_hammer", ItemTier.IRON),
				new ItemHammer("diamond_hammer", ItemTier.DIAMOND),
				new ItemEnderTotem(),
				new ItemStoneShears(),
				new ItemImpl("unfired_crucible"),
				new ItemImpl("crucible"),
				new ItemImpl("cobble_crucible"),
				new ItemLavaCrucible()
		);
	}
}
