package net.warcar.ope_ope_rework.abilities;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.entities.SphereEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.List;
import java.util.function.Predicate;

public class NagiHelper {
    public static boolean canHear(LivingEntity user, LivingEntity target) {
        if (user.hasEffect(ModEffects.SILENT.get())) {
            return false;
        }
        return canHear(user, target.position());
    }
    
    public static boolean canHear(LivingEntity user, Vector3d pos) {
        if (oneType(user, pos, null, SilentProjectile.class)) return false;
        if (oneType(user, pos, entity -> {
            IAbilityData abilityData = AbilityDataCapability.get(getOwner(entity));
            RoomAbility ability = abilityData.getEquippedAbility(RoomAbility.INSTANCE);
            return (ability != null && ((IRoomMixin) ability).isRRoom());
        }, RoomProjectile.class)) return false;
        return true;
    }

    private static LivingEntity getOwner(Entity user) {
        if (user instanceof RoomProjectile) {
            return ((RoomProjectile) user).getOwner();
        } else if (user instanceof SilentProjectile) {
            return ((SilentProjectile) user).getOwner();
        }
        return null;
    }

    private static <E extends SphereEntity> boolean oneType(LivingEntity user, Vector3d pos, Predicate<Entity> predicate, Class<E> entity) {
        if (predicate == null) {
            predicate = Predicates.alwaysTrue();
        }
        List<E> silentProj = user.level.getEntitiesOfClass(entity, new AxisAlignedBB(pos.add(-0.5, -0.5, -0.5), pos.add(0.5, 0.5, 0.5)).inflate(50), predicate);
        for (E roomProjectile : silentProj) {
            if ((roomProjectile.closerThan(user, roomProjectile.getRadius()) && !roomProjectile.position().closerThan(pos, roomProjectile.getRadius())) || (!roomProjectile.closerThan(user, roomProjectile.getRadius()) && roomProjectile.position().closerThan(pos, roomProjectile.getRadius()))) {
                return true;
            }
        }
        return false;
    }
}
