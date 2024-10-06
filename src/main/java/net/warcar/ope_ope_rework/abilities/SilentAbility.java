package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.projectiles.SilentProjectile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class SilentAbility extends Ability {
    private static final ITextComponent[] DESCRIPTION = AbilityHelper.registerDescriptionText(OpeReworkMod.MOD_ID, "silent", ImmutablePair.of("Creates silent zone around user, sound can't escape it or come in", null));
    public static final AbilityCore<SilentAbility> INSTANCE = new AbilityCore.Builder<>("Silent", AbilityCategory.DEVIL_FRUITS, SilentAbility::new).addDescriptionLine(DESCRIPTION).build();
    private final ChargeComponent chargeComponent;
    private final ContinuousComponent continuousComponent;
    private SilentProjectile silentProjectile;

    public SilentAbility(AbilityCore<SilentAbility> core) {
        super(core);
        chargeComponent = new ChargeComponent(this, true);
        continuousComponent = new ContinuousComponent(this, true);
        this.addUseEvent((entity, ability) -> {
            if (this.continuousComponent.isContinuous()) {
                this.continuousComponent.stopContinuity(entity);
            } else if (this.chargeComponent.isCharging()) {
                this.chargeComponent.stopCharging(entity);
            } else {
                this.chargeComponent.startCharging(entity, 40);
            }
        });
        this.chargeComponent.addEndEvent(this::onChargeStopped);
        this.addComponents(chargeComponent, continuousComponent);
        this.continuousComponent.addEndEvent(this::onContinuityStopped);
        this.isNew = true;
    }

    private void onChargeStopped(LivingEntity entity, IAbility ability) {
        if (silentProjectile != null) {
            silentProjectile.remove();
        }
        silentProjectile = new SilentProjectile(entity.level, entity);
        this.silentProjectile.setSize(this.chargeComponent.getChargeTime() / 4);
        entity.level.addFreshEntity(silentProjectile);
        silentProjectile.setPos(entity.getX(), entity.getY() - silentProjectile.getSize(), entity.getZ());
        silentProjectile.setPosAndOldPos(entity.getX(), entity.getY() - silentProjectile.getSize(), entity.getZ());
        continuousComponent.startContinuity(entity, -1);
    }

    private void onContinuityStopped(LivingEntity entity, IAbility ability) {
        this.cooldownComponent.startCooldown(entity, silentProjectile.getSize() * 20);
        silentProjectile.remove();
        silentProjectile = null;
    }
}
