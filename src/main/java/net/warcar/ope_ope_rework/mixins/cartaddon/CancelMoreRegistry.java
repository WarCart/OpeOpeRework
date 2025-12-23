package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.init.ReworkAbilities;
import net.warcar.ope_ope_rework.init.Abilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

@Mixin(ReworkAbilities.class)
public class CancelMoreRegistry {
    @Inject(method = "registerFruit", at = @At("HEAD"), remap = false, cancellable = true)
    private static <T extends AkumaNoMiItem> void notRegister(T fruit, CallbackInfoReturnable<T> cir) {
        if (fruit.getDevilFruitName().equalsIgnoreCase("Ope Ope No mi")) {
            cir.setReturnValue(fruit);
        }
    }

    @Shadow @Final public static final AkumaNoMiItem REWORED_OPE = Abilities.REAL_OPE;
}
