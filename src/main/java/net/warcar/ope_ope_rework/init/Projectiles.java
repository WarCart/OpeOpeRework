package net.warcar.ope_ope_rework.init;

import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.warcar.ope_ope_rework.models.SphereModel;
import net.warcar.ope_ope_rework.projectiles.KRoomProjectile;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import net.warcar.ope_ope_rework.render.KRoomRenderer;
import net.warcar.ope_ope_rework.render.RoomRenderer;
import net.warcar.ope_ope_rework.render.SilentRender;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Projectiles {
    public static final RegistryObject<EntityType<SilentProjectile>> SILENT = WyRegistry.registerEntityType("Silent", () -> WyRegistry.createEntityType(SilentProjectile::new).sized(10F, 10F).build("mineminenomi:t"));

    public static final RegistryObject<EntityType<RoomProjectile>> ROOM = WyRegistry.registerEntityType("Room", () -> WyRegistry.createEntityType(RoomProjectile::new).sized(80F, 80F).build("mineminenomi:t"));

    public static final RegistryObject<EntityType<KRoomProjectile>> K_ROOM = WyRegistry.registerEntityType("K-Room", () -> WyRegistry.createEntityType(KRoomProjectile::new).sized(1F, 1F).build("mineminenomi:t"));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(K_ROOM.get(), new KRoomRenderer.Factory());
        RenderingRegistry.registerEntityRenderingHandler(ROOM.get(), new RoomRenderer.Factory());
        RenderingRegistry.registerEntityRenderingHandler(SILENT.get(), new SilentRender.Factory());
    }
}
