package net.warcar.ope_ope_rework.mixins;

import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTickableSound.class)
public interface EntityTickableSoundMixin {
    @Accessor
    Entity getEntity();
}
