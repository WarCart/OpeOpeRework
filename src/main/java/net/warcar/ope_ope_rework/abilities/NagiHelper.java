package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.List;

public class NagiHelper {
    public static boolean canHear(LivingEntity user, LivingEntity target) {
        if (user.hasEffect(ModEffects.SILENT.get())) {
            return false;
        }
        List<SilentProjectile> silentProj = WyHelper.getNearbyEntities(user.blockPosition(), user.level, 10, null, SilentProjectile.class);
        if (!silentProj.isEmpty()) {
            for (SilentProjectile roomProjectile : silentProj) {
                if ((roomProjectile.closerThan(user, roomProjectile.getSize()) && !roomProjectile.position().closerThan(target.position(), roomProjectile.getSize())) || (!roomProjectile.closerThan(user, roomProjectile.getSize()) && roomProjectile.position().closerThan(target.position(), roomProjectile.getSize()))) {
                    return false;
                }
            }
        }
        List<RoomProjectile> roomProj = WyHelper.getNearbyEntities(user.blockPosition(), user.level, 40, null, RoomProjectile.class);
        if (!roomProj.isEmpty()) {
            for (RoomProjectile roomProjectile : roomProj) {
                IAbilityData abilityData = AbilityDataCapability.get(roomProjectile.getOwner());
                RoomAbility ability = abilityData.getEquippedAbility(RoomAbility.INSTANCE);
                if ((ability != null && ((IRoomMixin) ability).isRRoom()) && ((roomProjectile.closerThan(user, roomProjectile.getSize()) && !roomProjectile.position().closerThan(target.position(), roomProjectile.getSize())) || (!roomProjectile.closerThan(user, roomProjectile.getSize()) && roomProjectile.position().closerThan(target.position(), roomProjectile.getSize())))) {
                    return false;
                }
            }
        }
        return true;
    }
}
