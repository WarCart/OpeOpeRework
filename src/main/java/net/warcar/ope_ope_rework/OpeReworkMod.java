package net.warcar.ope_ope_rework;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.init.Abilities;
import net.warcar.ope_ope_rework.init.Animations;
import net.warcar.ope_ope_rework.init.Effects;
import net.warcar.ope_ope_rework.packets.SBonusManagerUpdatePacket;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

/** TODO: Bugs found:<br>
 * Cart's takt toss doesn't work (add reworked)
 */
@Mod(OpeReworkMod.MOD_ID)
public class OpeReworkMod {
    public static final String MOD_ID = "ope_ope_rework";
    public static final Logger LOGGER = LogManager.getLogger();

    public OpeReworkMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        bus.addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        Abilities.reg(bus);
        Effects.register();
        MinecraftForge.EVENT_BUS.register(this);
        WyNetwork.registerPacket(SBonusManagerUpdatePacket.class, SBonusManagerUpdatePacket::encode, SBonusManagerUpdatePacket::decode, SBonusManagerUpdatePacket::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(Animations::clientSetup);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    public static boolean isCartAddonLoaded() {
        return ModList.get().isLoaded("cartaddon");
    }
}
