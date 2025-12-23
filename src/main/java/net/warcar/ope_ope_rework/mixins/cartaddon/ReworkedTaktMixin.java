package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedTaktAbility;
import org.spongepowered.asm.mixin.Mixin;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

@Mixin(ReworkedTaktAbility.class)
public abstract class ReworkedTaktMixin extends Ability {
    private ReworkedTaktMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }
}
