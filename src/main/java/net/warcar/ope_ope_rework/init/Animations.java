package net.warcar.ope_ope_rework.init;

import net.minecraft.util.ResourceLocation;
import net.warcar.ope_ope_rework.OpeReworkMod;
import net.warcar.ope_ope_rework.animations.SlicedAnimation;
import xyz.pixelatedw.mineminenomi.api.animations.Animation;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public class Animations {
    public static final AnimationId<SlicedAnimation> UNCONSCIOUS = register("unconscious_halved");

    private static <A extends Animation<?, ?>> AnimationId<A> register(String name) {
        return new AnimationId<>(new ResourceLocation(OpeReworkMod.MOD_ID, name));
    }

    public static void clientSetup() {
        AnimationId.register(new SlicedAnimation(UNCONSCIOUS));
    }
}
