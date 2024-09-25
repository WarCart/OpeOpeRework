package net.warcar.ope_ope_rework.mixins;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.abilities.nagi.SilentAbility;

@Mixin(SilentAbility.SilentAbilityClientEvents.class)
public abstract class EventCancelerMixin {
    @Inject(method = "onLivingUpdate", at = @At("HEAD"), remap = false, cancellable = true)
    private static void noUpdate(LivingEvent.LivingUpdateEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onSoundPlayed", at = @At("HEAD"), remap = false, cancellable = true)
    private static void noSound(PlaySoundEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
