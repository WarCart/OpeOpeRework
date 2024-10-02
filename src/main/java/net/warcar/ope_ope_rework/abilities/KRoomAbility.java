package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.fruit_progression.data.entity.awakening.IAwakeningData;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.init.Abilities;
import net.warcar.ope_ope_rework.projectiles.KRoomProjectile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.DevilFruitHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

public class KRoomAbility extends ContinuousAbility {
    public static final AbilityCore<KRoomAbility> INSTANCE = new AbilityCore.Builder<>("K-Room", "k_room", AbilityCategory.DEVIL_FRUITS, KRoomAbility::new)
            .setSourceType(SourceType.INTERNAL).setSourceElement(SourceElement.SHOCKWAVE).setSourceHakiNature(SourceHakiNature.IMBUING)
            .setUnlockCheck(KRoomAbility::canUnlock).addDescriptionLine("").build();

    private boolean charging = false;
    private KRoomProjectile projectile;

    public KRoomAbility(AbilityCore<KRoomAbility> core) {
        super(core);
        this.onStartContinuityEvent = this::beforeUse;
    }

    private boolean beforeUse(PlayerEntity entity) {
        return ItemsHelper.isSword(entity.getMainHandItem());
    }

    private void onEnd(PlayerEntity entity) {
        if (this.projectile != null && this.projectile.isAlive()) {
            this.projectile.kill();
        }
    }

    private void onContinuousTick(PlayerEntity entity, int i) {
        entity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1, false, false));
    }

    private void onChargeTick(PlayerEntity entity, int time) {
        this.onContinuousTick(entity, time);
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
        if (this.isContinuous()) {
            this.charging = true;
            this.continueTime = 0;
            this.threshold = 40;
        } else if (!this.charging) {
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

    private KRoomProjectile factory(LivingEntity entity) {
        return new KRoomProjectile(entity.level, entity);
    }

    public boolean isShock() {
        return this.continueTime >= 10 && this.charging;
    }
}
