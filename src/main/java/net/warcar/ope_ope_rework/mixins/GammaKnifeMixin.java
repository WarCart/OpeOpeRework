package net.warcar.ope_ope_rework.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.ope.GammaKnifeAbility;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;

@Mixin(GammaKnifeAbility.class)
public abstract class GammaKnifeMixin {
    @Inject(method = "getPunchCooldown", at = @At("HEAD"), remap = false, cancellable = true)
    private void advancedCooldown(CallbackInfoReturnable<Float> cir) {
        PlayerEntity user = Minecraft.getInstance().player;
        if (user != null && AbilityDataCapability.get(user).getEquippedAbility(RoomAbility.INSTANCE) != null && AbilityDataCapability.get(user).getEquippedAbility(RoomAbility.INSTANCE).isPositionInRoom(user.blockPosition())) {
            cir.setReturnValue(1000f);
        }
    }
}
