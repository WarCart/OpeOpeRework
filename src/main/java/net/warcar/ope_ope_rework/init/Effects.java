package net.warcar.ope_ope_rework.init;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.warcar.ope_ope_rework.effects.OpePinnedEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

public class Effects {
    public static void register() {
    }

    public static final RegistryObject<Effect> OPE_PINNED = WyRegistry.registerEffect("Ope Pinned", OpePinnedEffect::new);
}
