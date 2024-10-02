package net.warcar.ope_ope_rework.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.abilities.IRoomMixin;
import net.warcar.ope_ope_rework.mixins.EntityTickableSoundMixin;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.List;

@Mod.EventBusSubscriber(modid = OpeReworkMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onSoundPlayed(PlaySoundEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Vector3d soundPos = new Vector3d(event.getSound().getX(), event.getSound().getY(), event.getSound().getZ());
            if (event.getSound() instanceof EntityTickableSoundMixin) {
                Entity entity = ((EntityTickableSoundMixin) event.getSound()).getEntity();
                if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(ModEffects.SILENT.get())) {
                    event.setResultSound(null);
                    return;
                }
            }
            if (mc.player.hasEffect(ModEffects.SILENT.get()) && soundPos.closerThan(mc.player.position(), 12.0D)) {
                event.setResultSound(null);
                return;
            }
            ClientPlayerEntity player = mc.player;
            List<SilentProjectile> silentProjectiles = WyHelper.getNearbyEntities(player.position(), mc.level, 40, null, SilentProjectile.class);
            if (!silentProjectiles.isEmpty()) {
                for (SilentProjectile silentProjectile : silentProjectiles) {
                    if ((silentProjectile.closerThan(player, silentProjectile.getSize()) && !silentProjectile.position().closerThan(soundPos, silentProjectile.getSize())) || (!silentProjectile.closerThan(player, silentProjectile.getSize()) && silentProjectile.position().closerThan(soundPos, silentProjectile.getSize()))) {
                        event.setResultSound(null);
                        return;
                    }
                }
            }

            if (roomSilencer(player, mc, soundPos)) {
                event.setResultSound(null);
            }
        }
    }

    private static boolean roomSilencer(ClientPlayerEntity player, Minecraft mc, Vector3d soundPos) {
        List<RoomProjectile> roomProj = WyHelper.getNearbyEntities(player.position(), mc.level, 40, null, RoomProjectile.class);
        if (!roomProj.isEmpty()) {
            for (RoomProjectile roomProjectile : roomProj) {
                if (roomProjectile.getOwner() != null) {
                    IAbilityData abilityData = AbilityDataCapability.get(roomProjectile.getOwner());
                    RoomAbility ability = abilityData.getEquippedAbility(RoomAbility.INSTANCE);
                    if ((ability != null && ((IRoomMixin) ability).isRRoom()) && ((roomProjectile.closerThan(player, roomProjectile.getSize()) && !roomProjectile.position().closerThan(soundPos, roomProjectile.getSize())) || (!roomProjectile.closerThan(player, roomProjectile.getSize()) && roomProjectile.position().closerThan(soundPos, roomProjectile.getSize())))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void chatMuter(ClientChatReceivedEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity sender = mc.level.getPlayerByUUID(event.getSenderUUID());
        if (sender != null && roomSilencer(mc.player, mc, sender.position())) {
            event.setCanceled(true);
        }
    }
}
