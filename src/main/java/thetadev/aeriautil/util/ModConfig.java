package thetadev.aeriautil.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig
{
	public static ModConfig instance;
	public ForgeConfigSpec.BooleanValue base_value;

	public ModConfig(ForgeConfigSpec.Builder builder) {
		builder.push("base");
		base_value = builder.define("base_value", false);
		builder.pop();
	}
}
