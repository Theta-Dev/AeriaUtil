package thetadev.aeriautil.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thetadev.aeriautil.registry.IModItem;
import thetadev.aeriautil.AeriaUtil;
import thetadev.aeriautil.registry.ModRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImpl extends Item implements IModItem
{
	private final String baseName;

	public ItemImpl(String baseName) {
		this(baseName, new Properties());
	}

	public ItemImpl(String baseName, Properties properties) {
		super(baseProperties(properties));
		this.baseName = baseName;
		ModRegistry.add(this);
	}

	@Override
	public String getBaseName() {
		return baseName;
	}

	public boolean hasTooltip() {
		return false;
	}

	public static Properties baseProperties() {
		return baseProperties(new Properties());
	}

	public static Properties baseProperties(Properties properties) {
		return properties.group(AeriaUtil.CREATIVE_TAB);
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(hasTooltip()) tooltip.add(new TranslationTextComponent(AeriaUtil.MOD_ID + ".tooltip." + baseName).applyTextStyle(TextFormatting.GRAY));
	}
}
