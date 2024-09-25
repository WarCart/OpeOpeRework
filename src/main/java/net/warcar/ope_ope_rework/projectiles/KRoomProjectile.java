package net.warcar.ope_ope_rework.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.ope_ope_rework.abilities.KRoomAbility;
import net.warcar.ope_ope_rework.init.Effects;
import net.warcar.ope_ope_rework.init.Projectiles;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

import java.util.ArrayList;
import java.util.List;

public class KRoomProjectile extends AbilityProjectileEntity {
    public final List<LivingEntity> targets = new ArrayList<>();

    public KRoomProjectile(World world, LivingEntity thrower) {
        super(Projectiles.K_ROOM.get(), world, thrower);
        this.setMaxLife(100000);
        this.setDamage(0);
        this.setPassThroughBlocks();
        this.setPassThroughEntities();
        this.onEntityImpactEvent = this::onImpact;
    }

    public KRoomProjectile(EntityType type, World world) {
        super(type, world);
    }

    private void onImpact(LivingEntity target) {
        this.targets.add(target);
        target.addEffect(new EffectInstance(Effects.OPE_PINNED.get(), 5, 1, false, false));
    }

    @Override
    public void tick() {
        if (this.getThrower() != null) {
            KRoomAbility ability = AbilityDataCapability.get(this.getThrower()).getEquippedAbility(KRoomAbility.INSTANCE);
            if (ability != null && ability.isCharging()) {
                this.setDeltaMovement(Vector3d.ZERO);
            }
        }
        if (this.getY() < 0) {
            this.setDeltaMovement(Vector3d.ZERO);
        }
        for (LivingEntity entity : this.targets) {
            entity.addEffect(new EffectInstance(Effects.OPE_PINNED.get(), 5, 1, false, false));
        }
        Vector3d preserveVel = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(preserveVel);
        if (this.getThrower() != null) {
            Vector3d vec = this.getThrower().position().vectorTo(this.position());
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec));
            this.xRot = lerpRotation(this.xRotO, (float) (MathHelper.atan2(vec.y, f) * (double) (180F / (float) Math.PI)));
            this.yRot = lerpRotation(this.yRotO, (float) (MathHelper.atan2(vec.x, vec.z) * (double) (180F / (float) Math.PI)));
        }
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }
}
