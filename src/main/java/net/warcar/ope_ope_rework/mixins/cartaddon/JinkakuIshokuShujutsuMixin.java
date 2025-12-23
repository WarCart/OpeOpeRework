package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.abilities.opeextra.JinkakuIshokuShujutsuAbility;
import net.MrMagicalCart.cartaddon.init.CartEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.entities.mobs.OPEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mixin(JinkakuIshokuShujutsuAbility.class)
public abstract class JinkakuIshokuShujutsuMixin extends Ability {
    @Shadow @Final private static Predicate<Entity> SHAMBLES_LIST;

    private JinkakuIshokuShujutsuMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Inject(method = "onUseEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void onUseEvent(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();

        super.cooldownComponent.startCooldown(entity, 300.0F);
        entity.level.playSound(null, entity.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.75F);
        RoomAbility reworkedRoomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        BlockPos centerPos;
        if (reworkedRoomAbility == null) {
            centerPos = entity.blockPosition();
        } else {
            centerPos = reworkedRoomAbility.getCenterBlock();
        }
        Vector3d centerVec = new Vector3d(centerPos.getX(), centerPos.getY(), centerPos.getZ());
        Predicate<Entity> groupCheck = ModEntityPredicates.getEnemyFactions(entity).and(SHAMBLES_LIST);
        List<Entity> targets = WyHelper.getNearbyEntities(centerVec, entity.level, reworkedRoomAbility != null ? reworkedRoomAbility.getROOMSize() : 50, groupCheck, Entity.class);
        Collections.shuffle(targets);

        for(int i = 0; i < targets.size(); ++i) {
            if (targets.get(i) instanceof PlayerEntity) {
                targets.remove(i);
            }
        }

        for(int i = 0; i < targets.size() && i + 1 < targets.size(); i += 2) {
            Entity target1 = targets.get(i);
            Entity target2 = targets.get(i + 1);
            if (reworkedRoomAbility == null || (reworkedRoomAbility.isPositionInRoom(target1.blockPosition()) && reworkedRoomAbility.isPositionInRoom(target2.blockPosition()))) {
                float[] beforeCoords = new float[]{(float)target2.getX(), (float)target2.getY(), (float)target2.getZ(), target2.yRot, target2.xRot};
                target2.moveTo(target1.getX(), target1.getY(), target1.getZ(), target1.yRot, target1.xRot);
                target2.moveTo(target1.getX(), target1.getY(), target1.getZ());
                target1.moveTo(beforeCoords[0], beforeCoords[1], beforeCoords[2], beforeCoords[3], beforeCoords[4]);
                if (target1 instanceof OPEntity) {
                    ((OPEntity)target1).addEffect(new EffectInstance(CartEffects.DISABLED_ABILITIES.get(), 60, 0));
                    ((OPEntity)target1).addEffect(new EffectInstance(ModEffects.NO_HANDS.get(), 60, 0));
                }

                if (target2 instanceof OPEntity) {
                    ((OPEntity)target2).addEffect(new EffectInstance(CartEffects.DISABLED_ABILITIES.get(), 60, 0));
                    ((OPEntity)target2).addEffect(new EffectInstance(ModEffects.NO_HANDS.get(), 60, 0));
                }

                entity.level.playSound(null, target2.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.75F);
                entity.level.playSound(null, target1.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.75F);
            }
        }
    }
}
