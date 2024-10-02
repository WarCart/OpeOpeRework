package net.warcar.ope_ope_rework.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ForgeMod;
import net.warcar.ope_ope_rework.init.Animations;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.effects.IDisableAbilitiesEffect;
import xyz.pixelatedw.mineminenomi.api.effects.ModEffect;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.packets.server.SToggleAnimationPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.util.function.Predicate;

public class SlicedEffect extends ModEffect implements IDisableAbilitiesEffect {
    public SlicedEffect() {
        super(EffectType.HARMFUL, WyHelper.hexToRGB("#000000").getRGB());
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "2727e176-e9e8-4523-92f8-22619308d9ee", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "323ffb58-0b57-434e-bdfc-354670e22d5f", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ModAttributes.JUMP_HEIGHT.get(), "e8cd65cb-2768-4fd8-aa54-bdcda029aaff", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "f8b2474d-4cdb-42b0-a868-327443a2a505", 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public void startEffect(LivingEntity entity, EffectInstance instance) {
        WyNetwork.sendToAllTrackingAndSelf(SToggleAnimationPacket.playAnimation(entity, Animations.UNCONSCIOUS, 10000000, true), entity);
    }

    public void stopEffect(LivingEntity entity) {
        WyNetwork.sendToAllTrackingAndSelf(SToggleAnimationPacket.stopAnimation(entity, Animations.UNCONSCIOUS), entity);
    }

    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    public boolean shouldRender(EffectInstance effect) {
        return false;
    }

    public boolean shouldRenderHUD(EffectInstance effect) {
        return false;
    }

    public boolean isRemoveable() {
        return false;
    }

    public Predicate<IAbility> getDisabledAbilities() {
        return (abl) -> true;
    }
}
