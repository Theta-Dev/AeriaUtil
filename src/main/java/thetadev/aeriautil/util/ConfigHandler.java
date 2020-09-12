package thetadev.aeriautil.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler
{
	private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.ConfigValue<String> BASALTGEN_BASE;

	static {
		builder.push("misc");
		BASALTGEN_BASE = builder.define("basaltGenBase", "minecraft:soul_sand");
		builder.pop();
	}

	public static final ForgeConfigSpec SPEC = builder.build();
}
