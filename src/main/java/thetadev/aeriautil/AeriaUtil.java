package thetadev.aeriautil;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thetadev.aeriautil.item.ModItems;
import thetadev.aeriautil.util.ConfigHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AeriaUtil.MOD_ID)
public class AeriaUtil
{
    public static final String MOD_ID = "aeriautil";
    public static final String MOD_NAME = "AeriaUtil";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static AeriaUtil instance;

    public AeriaUtil() {
        instance = this;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.SPEC);
    }

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ENDER_TOTEM);
        }
    };

    public static ResourceLocation loc(String resource) {
        return new ResourceLocation(MOD_ID, resource);
    }

    public void setup(FMLCommonSetupEvent event) {
        this.preInit(event);
        this.init(event);
        this.postInit(event);
    }

    public void preInit(FMLCommonSetupEvent event) {
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void postInit(FMLCommonSetupEvent event) {
    }
}
