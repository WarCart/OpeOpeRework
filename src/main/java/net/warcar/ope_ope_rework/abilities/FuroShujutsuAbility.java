package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.init.Abilities;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.AbilityDamageSource;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;


public class FuroShujutsuAbility extends Ability {
    private static final ITextComponent[] DESCRIPTION = AbilityHelper.registerDescriptionText(OpeReworkMod.MOD_ID, "furo_shujutsu", ImmutablePair.of("", null));
    public static final AbilityCore<FuroShujutsuAbility> INSTANCE = new AbilityCore.Builder<>("Furo Shujutsu", AbilityCategory.DEVIL_FRUITS, FuroShujutsuAbility::new)
            .addDescriptionLine(DESCRIPTION).setUnlockCheck(FuroShujutsuAbility::canUnlock).build();
    private final ContinuousComponent continuousComponent = new ContinuousComponent(this);
    private final HitTriggerComponent hitTriggerComponent = new HitTriggerComponent(this).addOnHitEvent(this::onHitEvent).setBypassSameGroupProtection();
    private final ChargeComponent chargeComponent = new ChargeComponent(this).addTickEvent(this::onChargeTick).addEndEvent(this::onChargeEnd);

    private LivingEntity target;

    public FuroShujutsuAbility(AbilityCore<FuroShujutsuAbility> core) {
        super(core);
        this.isNew = true;
        this.addUseEvent((livingEntity, ability) -> this.continuousComponent.triggerContinuity(livingEntity));
        this.addComponents(continuousComponent, chargeComponent, hitTriggerComponent);
    }

    private void onChargeEnd(LivingEntity livingEntity, IAbility ability) {
        if (this.target != null) {
            try {
                if (livingEntity instanceof ServerPlayerEntity) ((ServerPlayerEntity) livingEntity).getAdvancements()
                        .award(((ServerPlayerEntity) livingEntity).server.getAdvancements()
                        .getAdvancement(new ResourceLocation("ope_ope_rework:forever_young")), "compete_operation");
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            livingEntity.hurt(getSource(), Float.MAX_VALUE);
            this.target = null;
        }
    }

    private static DamageSource getSource() {
        return new AbilityDamageSource("ope_life", (Entity) null, INSTANCE) {
            @Override
            public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
                String s = "death.attack." + this.msgId;
                return new TranslationTextComponent(s, entityLivingBaseIn.getName());
            }
        }.setBypassFriendlyDamage().setUnavoidable().setInternal().bypassInvul();
    }

    private void onChargeTick(LivingEntity entity, IAbility ability) {
        entity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 100, 0));
        if (target != null) {
            this.target.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 100, 0));
            if (!this.target.isAlive()) {
                this.target = null;
                this.cooldownComponent.startCooldown(entity, this.chargeComponent.getChargeTime() * 2);
                this.chargeComponent.stopCharging(entity);
            }
        }
    }

    private static boolean canUnlock(LivingEntity entity) {
        IDevilFruit fruit = DevilFruitCapability.get(entity);
        return fruit.getDevilFruitItem() == Abilities.REAL_OPE && fruit.hasAwakenedFruit();
    }

    private void onHitEvent(LivingEntity livingEntity, LivingEntity target, ModDamageSource modDamageSource, IAbility ability) {
        if (this.continuousComponent.isContinuous()) {
            modDamageSource.setBypassFriendlyDamage();
            this.target = target;
            this.continuousComponent.stopContinuity(livingEntity);
            this.chargeComponent.startCharging(livingEntity, 24);
        }
    }
}
