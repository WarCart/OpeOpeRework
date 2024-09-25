package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.ope_ope_rework.init.Abilities;
import net.warcar.ope_ope_rework.projectiles.KRoomProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.PoolComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.DevilFruitHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

public class KRoomAbility extends Ability {
    public static final AbilityCore<KRoomAbility> INSTANCE = new AbilityCore.Builder<>("K-Room", "k_room", AbilityCategory.DEVIL_FRUITS, AbilityType.ACTION, KRoomAbility::new)
            .setUnlockCheck(KRoomAbility::canUnlock).build();
    private final ProjectileComponent projectileComponent;
    private final ChargeComponent chargeComponent;
    private final ContinuousComponent continuousComponent;
    private final PoolComponent poolComponent;

    public KRoomAbility(AbilityCore<KRoomAbility> core) {
        super(core);
        this.isNew = true;
        this.addCanUseCheck(this::canUse);
        this.addUseEvent(this::onStart);
        projectileComponent = new ProjectileComponent(this, this::factory);
        continuousComponent = new ContinuousComponent(this);
        chargeComponent = new ChargeComponent(this);
        poolComponent = new PoolComponent(this, Abilities.ROOMS);
        this.chargeComponent.addTickEvent(this::onChargeTick);
        this.chargeComponent.addEndEvent(this::onEnd);
        this.continuousComponent.addTickEvent(this::onContinuousTick);
        this.addComponents(projectileComponent, continuousComponent, chargeComponent, poolComponent);
    }

    private AbilityUseResult canUse(LivingEntity entity, IAbility ability) {
        if (ItemsHelper.isSword(entity.getMainHandItem())) {
            return AbilityUseResult.success();
        }
        return AbilityUseResult.fail(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_MELEE_WEAPON));
    }

    private void onEnd(LivingEntity entity, IAbility ability) {
        if (this.projectileComponent.hasProjectileAlive()) {
            this.projectileComponent.getShotProjectile().kill();
        }
        this.cooldownComponent.startCooldown(entity, 1600);
    }

    private void onContinuousTick(LivingEntity entity, IAbility ability) {
        entity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1, false, false));
    }

    private void onChargeTick(LivingEntity entity, IAbility ability) {
        this.onContinuousTick(entity, ability);
        if (this.chargeComponent.getChargeTime() < 10 && this.projectileComponent.hasProjectileAlive()) {
            Vector3d part = entity.position().vectorTo(this.projectileComponent.getShotProjectile().position());
            double offset = (part.length() / 10) * (10 - this.chargeComponent.getChargeTime());
            for (int i = 0; i < part.length() / 10; i++) {
                Vector3d currentPos = entity.position().add(part.normalize().multiply(i + offset, i + offset, i + offset));
                ExplosionAbility explosion = new ExplosionAbility(entity, entity.level, currentPos.x, currentPos.y, currentPos.z, 5);
                explosion.setDamageOwner(false);
                explosion.setFireAfterExplosion(false);
                explosion.setExplosionSound(false);
                explosion.doExplosion();
            }
        } else if (this.projectileComponent.hasProjectileAlive() && this.chargeComponent.getChargeTime() == 10) {
            for (LivingEntity target : ((KRoomProjectile) this.projectileComponent.getShotProjectile()).targets) {
                target.hurt(ModDamageSource.causeAbilityDamage(entity, ability).setPiercing(1).setUnavoidable().bypassLogia().bypassArmor(), 125);
            }
        }
    }

    private void onStart(LivingEntity entity, IAbility ability) {
        if (this.continuousComponent.isContinuous()) {
            this.continuousComponent.stopContinuity(entity);
            this.chargeComponent.startCharging(entity, 40);
        } else if (!this.chargeComponent.isCharging()) {
            this.projectileComponent.shoot(entity, 1, 0);
            this.continuousComponent.triggerContinuity(entity, -1);
        }
    }

    private static boolean canUnlock(LivingEntity entity) {
        IDevilFruit fruit = DevilFruitCapability.get(entity);
        return fruit.getDevilFruitItem() == Abilities.REAL_OPE && fruit.hasAwakenedFruit();
    }

    private KRoomProjectile factory(LivingEntity entity) {
        return new KRoomProjectile(entity.level, entity);
    }

    public boolean isShock() {
        return this.chargeComponent.getChargeTime() >= 10 && this.chargeComponent.isCharging();
    }
}
