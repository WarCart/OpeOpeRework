package net.warcar.ope_ope_rework.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.pixelatedw.mineminenomi.abilities.nagi.SilentAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

@Mixin(SilentAbility.class)
public abstract class SilentMixin {
    @Shadow
    @Final
    public static final AbilityCore INSTANCE = net.warcar.ope_ope_rework.abilities.SilentAbility.INSTANCE;
}
