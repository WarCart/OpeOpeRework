package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.HitAbility;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

public class CalmAbility extends HitAbility {
    public static final AbilityCore<CalmAbility> INSTANCE = new AbilityCore.Builder<>("Calm", AbilityCategory.DEVIL_FRUITS, CalmAbility::new).addDescriptionLine("Cancels all noises caused by or around the user.").build();

    private LivingEntity target = null;
    public CalmAbility(AbilityCore<CalmAbility> core) {
        super(core);
        this.onHitEntityEvent = this::onHitEffect;
        this.duringContinuityEvent = this::duringContinuity;
        this.afterContinuityStopEvent = this::afterContinuityStop;
        this.onStartContinuityEvent = this::onStartContinuity;
        this.setMaxCooldown(100);
        this.setStoppingAfterHit(false);
    }

    public float onHitEffect(PlayerEntity entity, LivingEntity target) {
        if (this.target == null) {
            this.target = target;
            this.target.setSilent(true);
            WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(entity, this), entity);
            return -1;
        } else {
            return 0;
        }
    }

    private boolean onStartContinuity(PlayerEntity entity) {
        if (entity.isSteppingCarefully()) {
            this.target = entity;
        }
        return true;
    }

    private void afterContinuityStop(PlayerEntity entity) {
        if (target != null) {
            this.target.setSilent(false);
        }
        this.target = null;
    }

    private void duringContinuity(PlayerEntity livingEntity, int i) {
        if (target != null) {
            target.addEffect(new EffectInstance(ModEffects.SILENT.get(), 20, 0, false, false));
        }
    }
}
