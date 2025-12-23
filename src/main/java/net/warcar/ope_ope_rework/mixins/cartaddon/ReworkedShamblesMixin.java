package net.warcar.ope_ope_rework.mixins.cartaddon;

import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedRoomAbility;
import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedShamblesAbility;
import net.MrMagicalCart.cartaddon.init.CartEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.protection.DefaultProtectionRules;
import xyz.pixelatedw.mineminenomi.api.protection.block.RestrictedBlockProtectionRule;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.world.ProtectedAreasData;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mixin(ReworkedShamblesAbility.class)
public abstract class ReworkedShamblesMixin extends Ability {
    @Shadow @Final private static Predicate<Entity> SHAMBLES_LIST;

    @Shadow @Final private AltModeComponent<ReworkedShamblesAbility.Mode> altModeComponent;

    private ReworkedShamblesMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Inject(method = "onUseEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void onUseEvent(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();
        RoomAbility reworkedRoomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        boolean hadTarget = false;
        if (this.altModeComponent.getCurrentMode() == ReworkedShamblesAbility.Mode.SINGLE) {
            RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, reworkedRoomAbility.getROOMSize());
            if (mop instanceof EntityRayTraceResult) {
                EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)mop;
                Entity target = entityRayTraceResult.getEntity();
                if (!reworkedRoomAbility.isEntityInRoom(target)) {
                    return;
                }

                if (!SHAMBLES_LIST.test(target)) {
                    return;
                }

                float[] beforeCoords = new float[]{(float)entity.getX(), (float)entity.getY(), (float)entity.getZ(), entity.yRot, entity.xRot};
                entity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
                entity.moveTo(target.getX(), target.getY(), target.getZ());
                target.moveTo(beforeCoords[0], beforeCoords[1], beforeCoords[2], beforeCoords[3], beforeCoords[4]);
                entity.level.playSound(null, entity.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 2.0F, 1.0F);
                entity.level.playSound(null, target.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 2.0F, 1.0F);
                if (!(target instanceof PlayerEntity) && target instanceof LivingEntity) {
                    ((LivingEntity)target).addEffect(new EffectInstance(CartEffects.DISABLED_ABILITIES.get(), 20, 0));
                }

                hadTarget = true;
            } else if (mop instanceof BlockRayTraceResult) {
                BlockRayTraceResult result = (BlockRayTraceResult)mop;
                BlockPos pos = result.getBlockPos();
                BlockState state = entity.level.getBlockState(pos);
                BlockPos entityPos = entity.blockPosition();
                BlockState entityPosState = entity.level.getBlockState(entityPos);
                boolean isInsideRoom = reworkedRoomAbility.isPositionInRoom(pos);
                boolean isDestinationBanned = RestrictedBlockProtectionRule.INSTANCE.isBanned(state);
                boolean isOriginBanned = RestrictedBlockProtectionRule.INSTANCE.isBanned(entityPosState);
                if (isInsideRoom && !isDestinationBanned && !isOriginBanned) {
                    BlockPos beforePos = entity.blockPosition();
                    ProtectedAreasData protectedAreaData = ProtectedAreasData.get(entity.level);
                    boolean a1 = protectedAreaData.isInsideRestrictedArea(beforePos.getX(), beforePos.getY(), beforePos.getZ());
                    boolean a2 = protectedAreaData.isInsideRestrictedArea(pos.getX(), pos.getY(), pos.getZ());
                    if (a1 != a2) {
                        return;
                    }

                    entity.moveTo(pos.getX(), pos.getY() + 1, pos.getZ(), entity.yRot, entity.xRot);
                    boolean b1 = AbilityHelper.placeBlockIfAllowed(entity, beforePos, state, 3, DefaultProtectionRules.AIR_CORE_FOLIAGE_ORE);
                    boolean b2 = AbilityHelper.placeBlockIfAllowed(entity, pos, Blocks.AIR.defaultBlockState(), 3, DefaultProtectionRules.AIR_CORE_FOLIAGE_ORE);
                    if (b1 && b2) {
                        entity.level.playSound(null, entity.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                        hadTarget = true;
                    }
                }
            }
        } else if (this.altModeComponent.getCurrentMode() == ReworkedShamblesAbility.Mode.GROUP) {
            BlockPos centerPos = reworkedRoomAbility.getCenterBlock();
            Vector3d centerVec = new Vector3d(centerPos.getX(), centerPos.getY(), centerPos.getZ());
            Predicate<Entity> groupCheck = ModEntityPredicates.getEnemyFactions(entity).and(SHAMBLES_LIST);
            List<Entity> targets = WyHelper.getNearbyEntities(centerVec, entity.level, reworkedRoomAbility.getROOMSize(), groupCheck, Entity.class);
            Collections.shuffle(targets);

            for(int i = 0; i < targets.size() && i < targets.size() && i + 1 < targets.size(); i += 2) {
                Entity target1 = targets.get(i);
                Entity target2 = targets.get(i + 1);
                if (reworkedRoomAbility.isPositionInRoom(target1.blockPosition()) && reworkedRoomAbility.isPositionInRoom(target2.blockPosition())) {
                    float[] beforeCoords = new float[]{(float)target2.getX(), (float)target2.getY(), (float)target2.getZ(), target2.yRot, target2.xRot};
                    target2.moveTo(target1.getX(), target1.getY(), target1.getZ(), target1.yRot, target1.xRot);
                    target2.moveTo(target1.getX(), target1.getY(), target1.getZ());
                    target1.moveTo(beforeCoords[0], beforeCoords[1], beforeCoords[2], beforeCoords[3], beforeCoords[4]);
                    entity.level.playSound(null, target2.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                    entity.level.playSound(null, target1.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
            }

            if (targets.size() >= 2) {
                hadTarget = true;
            }
        }

        float percentHp = entity.getHealth() * 100.0F / entity.getMaxHealth();
        if (hadTarget) {
            super.cooldownComponent.startCooldown(entity, 80.0F + 2.0F * (100.0F - percentHp));
        }
    }
}
