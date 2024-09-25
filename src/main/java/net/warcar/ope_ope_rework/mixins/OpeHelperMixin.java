package net.warcar.ope_ope_rework.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.ope_ope_rework.config.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.ope.OpeHelper;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityUseResult;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

@Mixin(OpeHelper.class)
public abstract class OpeHelperMixin {
    @Inject(method = "hasRoomActive", at = @At("HEAD"), remap = false, cancellable = true)
    private static void isNewRoom(LivingEntity entity, IAbility ability, CallbackInfoReturnable<AbilityUseResult> cir) {
        RoomAbility roomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        cir.setReturnValue(((roomAbility != null && roomAbility.isPositionInRoom(entity.blockPosition())) || (DevilFruitCapability.get(entity).hasAwakenedFruit() && CommonConfig.INSTANCE.isOutsideAbilities())) ? AbilityUseResult.success() : AbilityUseResult.fail(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_ONLY_IN_ROOM, ability.getDisplayName())));
    }
}
