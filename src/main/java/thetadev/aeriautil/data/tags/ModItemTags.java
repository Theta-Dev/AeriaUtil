package thetadev.aeriautil.data.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import thetadev.aeriautil.AeriaUtil;

public class ModItemTags
{
	public static final Tag<Item> COMPOSTABLE = makeWrapperTag("compostable");
	public static final Tag<Item> DIRT = makeWrapperTag("dirt");
	public static final Tag<Item> HAMMERS = makeWrapperTag("hammers");

	private static Tag<Item> makeWrapperTag(String name) {
		return new ItemTags.Wrapper(AeriaUtil.loc(name));
	}
}