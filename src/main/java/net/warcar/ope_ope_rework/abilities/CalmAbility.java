package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.PunchAbility2;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

import java.util.function.Predicate;

public class CalmAbility extends PunchAbility2 {
    private static final ITextComponent[] DESCRIPTION = AbilityHelper.registerDescriptionText("mineminenomi", "silent", ImmutablePair.of("Cancels all noises caused by or around the user.", null));
    public static final AbilityCore<CalmAbility> INSTANCE = new AbilityCore.Builder<>("Calm", AbilityCategory.DEVIL_FRUITS, CalmAbility::new).addDescriptionLine(DESCRIPTION).build();

    private LivingEntity target = null;
    public CalmAbility(AbilityCore<CalmAbility> core) {
        super(core);
        this.hitTriggerComponent.setBypassSameGroupProtection();
        this.continuousComponent.addTickEvent((livingEntity, iAbility) -> {
            if (target != null) {
                target.addEffect(new EffectInstance(ModEffects.SILENT.get(), 20, 0, false, false));
            }
        });
        this.continuousComponent.addEndEvent((entity, iAbility) -> {
            if (target != null) {
                this.target.setSilent(false);
            }
            this.target = null;
        });
        this.addUseEvent((entity, ability) -> {
            if (entity.isSteppingCarefully()) {
                this.target = entity;
                this.continuousComponent.stopContinuity(entity);
            }
        });
    }

    public float getPunchCooldown() {
        return 100;
    }

    public void onHitEffect(LivingEntity entity, LivingEntity target, ModDamageSource modDamageSource) {
        this.target = target;
        this.target.setSilent(true);
    }

    public Predicate<LivingEntity> canActivate() {
        return entity -> this.isContinuous() && this.target == null;
    }

    @Override
    public float getPunchDamage() {
        return -1;
    }

    public int getUseLimit() {
        return 0;
    }
}
