package thetadev.aeriautil.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.block.*;
import thetadev.aeriautil.block.tiles.ModTileEntities;
import thetadev.aeriautil.item.ModItems;
import thetadev.aeriautil.loot.LootModifierStick;
import thetadev.aeriautil.loot.LootModifierHammer;
import thetadev.aeriautil.recipes.ModRecipes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry
{
	public static final List<IModItem> ALL_ITEMS = new ArrayList<>();

	public static void add(IModItem item) {
		ALL_ITEMS.add(item);
		item.getRegistryEntry().setRegistryName(item.getBaseName());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event);
		populateObjectHolders(ModBlocks.class, event.getRegistry());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for (IModItem block : ALL_ITEMS) {
			if(block instanceof Block) {
				BlockItem item = new BlockItem((Block) block, new Item.Properties().group(AeriaUtil.CREATIVE_TAB));
				item.setRegistryName(block.getBaseName());
				event.getRegistry().register(item);
			}
		}

		ModItems.register(event);
		populateObjectHolders(ModItems.class, event.getRegistry());
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
		for (IModItem item : ALL_ITEMS) {
			if (item instanceof ModTileType)
				event.getRegistry().register(((ModTileType) item).type);
		}
		populateObjectHolders(ModTileEntities.class, event.getRegistry());
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		ModRecipes.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerModifierSerializers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
		event.getRegistry().registerAll(
				new LootModifierHammer.Serializer(),
				new LootModifierStick.Serializer()
		);
	}

	private static <T extends IForgeRegistryEntry<T>> void populateObjectHolders(Class clazz, IForgeRegistry<T> registry) {
		for (Field entry : clazz.getFields()) {
			if (!Modifier.isStatic(entry.getModifiers()))
				continue;
			ResourceLocation location = new ResourceLocation(AeriaUtil.MOD_ID, entry.getName().toLowerCase(Locale.ROOT));
			if (!registry.containsKey(location)) {
				AeriaUtil.LOGGER.fatal("Couldn't find entry named " + location + " in registry " + registry.getRegistryName());
				continue;
			}
			try {
				entry.set(null, registry.getValue(location));
			} catch (IllegalAccessException e) {
				AeriaUtil.LOGGER.error(e);
			}
		}
	}
}
