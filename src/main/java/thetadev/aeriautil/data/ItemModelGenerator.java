package thetadev.aeriautil.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.registry.ICustomItemModel;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.registry.INoItemBlock;
import thetadev.aeriautil.registry.ModRegistry;

import javax.annotation.Nonnull;

public class ItemModelGenerator extends ItemModelProvider
{
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, AeriaUtil.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for(IModItem modItem : ModRegistry.ALL_ITEMS) {
			String name = modItem.getBaseName();
			if(modItem instanceof ICustomItemModel) {
				((ICustomItemModel) modItem).generateCustomItemModel(this);
			}
			else if(modItem instanceof Item) {
				withExistingParent(name, "item/generated").texture("layer0", "item/" + name);
			}
			else if(modItem instanceof Block && !(modItem instanceof INoItemBlock)) {
				withExistingParent(name, this.modLoc("block/" + name));
			}
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return AeriaUtil.MOD_NAME + " item models";
	}
}
