package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.fruit_progression.data.entity.awakening.IAwakeningData;
import net.warcar.ope_ope_rework.projectiles.KRoomProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

public class KRoomAbility extends ContinuousAbility implements IExtraUpdateData {
    public static final AbilityCore<KRoomAbility> INSTANCE = new AbilityCore.Builder<>("K-Room", AbilityCategory.DEVIL_FRUITS, KRoomAbility::new)
            .setSourceType(SourceType.INTERNAL).setSourceElement(SourceElement.SHOCKWAVE).setSourceHakiNature(SourceHakiNature.IMBUING)
            .setUnlockCheck(KRoomAbility::canUnlock).addDescriptionLine("").setAbilityId("k_room").build();

    private boolean charging = false;
    private KRoomProjectile projectile;

    public KRoomAbility(AbilityCore<KRoomAbility> core) {
        super(core);
        this.onStartContinuityEvent = this::beforeUse;
        this.afterContinuityStopEvent = this::onEnd;
        this.beforeContinuityStopEvent = playerEntity -> {
            if (this.charging && this.getContinueTime() < threshold) {
                return false;
            } else if (!this.charging) {
                this.charging = true;
                this.continueTime = 0;
                this.threshold = 40;
                WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(playerEntity, this), playerEntity);
                return false;
            }
            return true;
        };
        this.duringContinuityEvent = (playerEntity, i) -> {
            if (this.charging) {
                this.onChargeTick(playerEntity, i);
            } else {
                this.onContinuousTick(playerEntity);
            }
        };
    }

    private boolean beforeUse(PlayerEntity entity) {
        if (ItemsHelper.isSword(entity.getMainHandItem())) {
            this.onStart(entity);
            return true;
        } else {
            return false;
        }
    }

    private void onEnd(PlayerEntity entity) {
        if (this.projectile != null && this.projectile.isAlive()) {
            this.projectile.kill();
        }
        this.charging = false;
    }

    private void onContinuousTick(PlayerEntity entity) {
        entity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1, false, false));
    }

    private void onChargeTick(PlayerEntity entity, int time) {
        this.onContinuousTick(entity);
        if (time < 10 && this.projectile != null && this.projectile.isAlive()) {
            Vector3d part = entity.position().vectorTo(this.projectile.position());
            double offset = (part.length() / 10) * (10 - time);
            for (int i = 0; i < part.length() / 10; i++) {
                Vector3d currentPos = entity.position().add(part.normalize().multiply(i + offset, i + offset, i + offset));
                ExplosionAbility explosion = new ExplosionAbility(entity, entity.level, currentPos.x, currentPos.y, currentPos.z, 5);
                explosion.setDamageOwner(false);
                explosion.setFireAfterExplosion(false);
                explosion.setExplosionSound(false);
                explosion.doExplosion();
            }
        } else if (this.isShock() && this.projectile != null) {
            for (LivingEntity target : this.projectile.targets) {
                target.hurt(ModDamageSource.causeAbilityDamage(entity, this).setInternal().bypassLogia().bypassArmor(), 125);
            }
        }
    }

    private void onStart(PlayerEntity entity) {
        if (!this.charging) {
            this.threshold = -1;
            this.startContinuity(entity);
            this.projectile = new KRoomProjectile(entity.level, entity);
            projectile.shootFromRotation(entity, entity.xRot, entity.yRot, 0, 2, 0);
            entity.level.addFreshEntity(projectile);
        }
    }

    private static boolean canUnlock(LivingEntity entity) {
        IAwakeningData fruit = AwakeningDataCapability.get(entity);
        return fruit.isAwakened();
    }

    public boolean isShock() {
        return this.continueTime >= 10 && this.charging;
    }

    public CompoundNBT getExtraData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("charging", this.charging);
        return nbt;
    }

    public void setExtraData(CompoundNBT compoundNBT) {
        this.charging = compoundNBT.getBoolean("charging");
    }
}
