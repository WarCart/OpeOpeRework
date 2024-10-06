package net.warcar.ope_ope_rework.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.ope_ope_rework.config.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.ope.OpeHelper;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;

@Mixin(OpeHelper.class)
public abstract class OpeHelperMixin {
    @Inject(method = "hasRoomActive", at = @At("HEAD"), remap = false, cancellable = true)
    private static void isNewRoom(PlayerEntity entity, Ability inst, CallbackInfoReturnable<Boolean> cir) {
        RoomAbility roomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        cir.setReturnValue(((roomAbility != null && roomAbility.isPositionInRoom(entity.blockPosition())) || (AwakeningDataCapability.get(entity).isAwakened() && CommonConfig.INSTANCE.isOutsideAbilities())));
    }
}
