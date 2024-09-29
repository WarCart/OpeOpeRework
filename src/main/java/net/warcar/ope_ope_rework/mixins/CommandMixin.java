package net.warcar.ope_ope_rework.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.warcar.ope_ope_rework.abilities.NagiHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.abilities.CommandAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent;
import xyz.pixelatedw.mineminenomi.api.entities.ICommandReceiver;
import xyz.pixelatedw.mineminenomi.api.enums.NPCCommand;
import xyz.pixelatedw.mineminenomi.api.util.TargetsPredicate;

@Mixin(CommandAbility.class)
public abstract class CommandMixin<E extends MobEntity & ICommandReceiver> extends Ability {
    @Shadow private TargetsPredicate targetPredicate;

    @Shadow @Final protected RangeComponent rangeComponent;

    protected CommandMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Shadow protected abstract void spawnCommandMark(LivingEntity entity, LivingEntity target);

    @Shadow public abstract NPCCommand getCommand();

    @Shadow public abstract void sendCommand(LivingEntity commandSender, E commandListener);

    @Inject(method = "onUseEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void silentUse(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        this.targetPredicate.selector((living) -> living instanceof MobEntity && living instanceof ICommandReceiver && ((ICommandReceiver)living).canReceiveCommandFrom(entity) && NagiHelper.canHear(entity, living));
        this.rangeComponent.getTargetsInArea(entity, 20.0F, this.targetPredicate).stream().limit(20L).forEach((target) -> {
            this.spawnCommandMark(entity, target);
            ((ICommandReceiver)target).setCurrentCommand(entity, this.getCommand());
            this.sendCommand(entity, (E) target);
        });
        this.spawnCommandMark(entity, entity);
        this.cooldownComponent.startCooldown(entity, 10.0F);
        ci.cancel();
    }
}
