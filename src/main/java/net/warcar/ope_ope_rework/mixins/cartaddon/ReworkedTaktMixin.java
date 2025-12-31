package net.warcar.ope_ope_rework.mixins.cartaddon;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedRoomAbility;
import net.MrMagicalCart.cartaddon.abilities.opeextra.ReworkedTaktAbility;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.Hand;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.protection.ProtectedArea;
import xyz.pixelatedw.mineminenomi.api.protection.block.RestrictedBlockProtectionRule;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.world.ProtectedAreasData;
import xyz.pixelatedw.mineminenomi.entities.projectiles.ope.TaktBlockEntity;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(ReworkedTaktAbility.class)
public abstract class ReworkedTaktMixin extends Ability {
    @Shadow private List<Entity> grabbedEntities;

    @Shadow @Final private ContinuousComponent continuousComponent;

    @Shadow protected static Predicate<BlockPos> isPositionGriefable(LivingEntity entity) {
        throw new UnsupportedOperationException("Mixin not mixin-ing :(");
    }

    private ReworkedTaktMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Inject(method = "onUseEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void onUseEvent$opeRework(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();
        RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, abl.getROOMSize());
        BlockPos blockPos = new BlockPos(mop.getLocation());
        if (mop.getType() == RayTraceResult.Type.BLOCK) {
            blockPos = new BlockPos(((BlockRayTraceResult)mop).getBlockPos());
        } else if (mop.getType() == RayTraceResult.Type.ENTITY) {
            blockPos = new BlockPos(((EntityRayTraceResult)mop).getEntity().blockPosition());
        }

        Function<BlockPos, TaktBlockEntity> mapper = (pos) -> {
            BlockState state = entity.level.getBlockState(pos);
            TaktBlockEntity fallingBlock = new TaktBlockEntity(entity.level, pos.getX(), pos.getY(), pos.getZ(), state);
            AbilityHelper.setDeltaMovement(fallingBlock, 0.0F, 0.0F, 0.0F);
            fallingBlock.time = 5;
            fallingBlock.setNoGravity(true);
            fallingBlock.dropItem = false;
            entity.level.addFreshEntity(fallingBlock);
            entity.level.removeBlock(pos, true);
            return fallingBlock;
        };
        Stream var10000 = WyHelper.getNearbyBlocks(blockPos, entity.level, 2, isPositionGriefable(entity), ImmutableList.of(Blocks.AIR)).stream().map(mapper);
        List var10001 = this.grabbedEntities;
        var10000.forEach(var10001::add);
        var10000 = WyHelper.getNearbyLiving(mop.getLocation(), entity.level, 2.0F, ModEntityPredicates.getEnemyFactions(entity)).stream().filter(ModEntityPredicates.IS_ALIVE_AND_SURVIVAL).filter((living) -> abl.isPositionInRoom(living.blockPosition()));
        var10001 = this.grabbedEntities;
        var10000.forEach(var10001::add);
        if (!this.grabbedEntities.isEmpty()) {
            this.continuousComponent.triggerContinuity(entity, 60.0F);
        }
    }

    @Inject(method = "onContinuityTick", at = @At("HEAD"), remap = false, cancellable = true)
    public void onContinuityTick$opeRework(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();
        if (!entity.level.isClientSide) {
            if (!super.canUse(entity).isFail() && !this.grabbedEntities.isEmpty()) {
                RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
                this.grabbedEntities.stream().forEach((target) -> {
                    target.xRot = target.xRotO;
                    target.yRot = target.yRotO;
                    Random rand = new Random(target.hashCode());
                    double offsetX = WyHelper.randomWithRange(rand, -2, 2);
                    double offsetY = WyHelper.randomWithRange(rand, -2, 2);
                    double offsetZ = WyHelper.randomWithRange(rand, -2, 2);
                    double distance = 8.0F;
                    Vector3d lookVec = entity.getLookAngle();
                    Vector3d pos = new Vector3d(lookVec.x * distance + offsetX, (double)entity.getEyeHeight() / (double)2.0F + lookVec.y * distance + offsetY, lookVec.z * distance + offsetZ);
                    if (target instanceof LivingEntity && abl.isPositionInRoom(target.blockPosition()) || isPositionGriefable(entity).test(target.blockPosition())) {
                        AbilityHelper.setDeltaMovement(target, entity.position().add(pos).subtract(target.position()));
                        if (target instanceof ServerPlayerEntity) {
                            Set<SPlayerPositionLookPacket.Flags> flags = EnumSet.of(SPlayerPositionLookPacket.Flags.X, SPlayerPositionLookPacket.Flags.Y, SPlayerPositionLookPacket.Flags.Z);
                            ((ServerPlayerEntity)target).connection.teleport(target.getX(), target.getY(), target.getZ(), target.yRotO, target.xRotO, flags);
                        }
                    }

                    target.fallDistance = 0.0F;
                });
            } else {
                this.continuousComponent.stopContinuity(entity);
            }
        }
    }

    @Inject(method = "isPositionGriefable", at = @At("HEAD"), remap = false, cancellable = true)
    private static void isPositionGriefable$opeRework(LivingEntity entity, CallbackInfoReturnable<Predicate<BlockPos>> cir) {
        RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        ProtectedAreasData worldData = ProtectedAreasData.get(entity.level);
        cir.setReturnValue((pos) -> {
            boolean isGriefDisabled = !CommonConfig.INSTANCE.isAbilityGriefingEnabled();
            if (isGriefDisabled) {
                return false;
            } else {
                ProtectedArea area = worldData.getProtectedArea(pos.getX(), pos.getY(), pos.getZ());
                if (area != null && !area.canDestroyBlocks()) {
                    return false;
                } else if (!abl.isPositionInRoom(pos)) {
                    return false;
                } else {
                    BlockState state = entity.level.getBlockState(pos);
                    boolean isBlockBanned = RestrictedBlockProtectionRule.INSTANCE.isBanned(state);
                    return !isBlockBanned;
                }
            }
        });
    }
}
