package net.warcar.ope_ope_rework.events;

import net.minecraft.entity.EntitySize;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;

@Mod.EventBusSubscriber(modid = OpeReworkMod.MOD_ID)
public class RoomEvents {
    @SubscribeEvent
    public static void roomSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof RoomProjectile) {
            event.setNewSize(EntitySize.scalable(400, 400));
        }
    }
}
