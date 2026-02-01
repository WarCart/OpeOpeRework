package net.warcar.ope_ope_rework.abilities;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.network.play.server.SPlayerPositionLookPacket.Flags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.warcar.ope_ope_rework.projectiles.FloatingBlockEntity;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.abilities.ope.OpeHelper;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityDescriptionLine;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.CooldownComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.protection.ProtectedArea;
import xyz.pixelatedw.mineminenomi.api.protection.block.RestrictedBlockProtectionRule;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.world.ProtectedAreasData;
import xyz.pixelatedw.mineminenomi.entities.projectiles.ope.TaktBlockEntity;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class ReworkedTaktAbility extends Ability {
    private static final ITextComponent[] DESCRIPTION = AbilityHelper.registerDescriptionText("mineminenomi", "takt", new Pair[]{ImmutablePair.of("The user lifts entities its looking at, preventing them from moving freely.", null)});
    private static final float COOLDOWN = 240.0F;
    private static final float HOLD_TIME = 60.0F;
    public static final AbilityCore<ReworkedTaktAbility> INSTANCE;
    private final ContinuousComponent continuousComponent = (new ContinuousComponent(this)).addTickEvent(this::onContinuityTick).addEndEvent(this::onContinuityEnd);
    private final List<Entity> grabbedEntities = new ArrayList<>();

    public ReworkedTaktAbility(AbilityCore<ReworkedTaktAbility> core) {
        super(core);
        super.isNew = true;
        super.addComponents(this.continuousComponent);
        super.addCanUseCheck(OpeHelper::hasRoomActive);
        super.addUseEvent(this::onUseEvent);
    }

    private void onUseEvent(LivingEntity entity, IAbility ability) {
        RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        int roomSize;
        if (abl != null) {
            roomSize = abl.getROOMSize();
        } else {
            roomSize = net.warcar.ope_ope_rework.config.CommonConfig.INSTANCE.getMaxRoomSize();
        }
        RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, roomSize);
        BlockPos blockPos = new BlockPos(mop.getLocation());
        if (mop.getType() == Type.BLOCK) {
            blockPos = new BlockPos(((BlockRayTraceResult)mop).getBlockPos());
        } else if (mop.getType() == Type.ENTITY) {
            blockPos = new BlockPos(((EntityRayTraceResult)mop).getEntity().blockPosition());
        }

        Function<BlockPos, FloatingBlockEntity> mapper = (pos) -> {
            FloatingBlockEntity fallingBlock = new FloatingBlockEntity(entity.level, pos);
            AbilityHelper.setDeltaMovement(fallingBlock, 0.0F, 0.0F, 0.0F);
            fallingBlock.setNoGravity(true);
            fallingBlock.dropItem = false;
            entity.level.addFreshEntity(fallingBlock);
            entity.level.removeBlock(pos, true);
            return fallingBlock;
        };
        WyHelper.getNearbyBlocks(blockPos, entity.level, 2, isPositionGriefable(entity), ImmutableList.of(Blocks.AIR)).stream().map(mapper)
                .forEach(this.grabbedEntities::add);
        WyHelper.getNearbyLiving(mop.getLocation(), entity.level, 2, ModEntityPredicates.getEnemyFactions(entity))
                .stream().filter(ModEntityPredicates.IS_ALIVE_AND_SURVIVAL).filter((living) -> isPositionInRoom(entity, abl, living.blockPosition()))
                .forEach(this.grabbedEntities::add);
        if (!this.grabbedEntities.isEmpty()) {
            this.continuousComponent.triggerContinuity(entity, HOLD_TIME);
        }

    }

    private void onContinuityTick(LivingEntity entity, IAbility ability) {
        if (!entity.level.isClientSide) {
            if (!super.canUse(entity).isFail() && !this.grabbedEntities.isEmpty()) {
                RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
                this.grabbedEntities.stream().forEach((target) -> {
                    Random rand = new Random(target.hashCode());
                    target.xRot = target.xRotO + rand.nextFloat() * 15;
                    target.yRot = target.yRotO + rand.nextFloat() * 15;
                    double offsetX = WyHelper.randomWithRange(rand, -2, 2);
                    double offsetY = WyHelper.randomWithRange(rand, -2, 2);
                    double offsetZ = WyHelper.randomWithRange(rand, -2, 2);
                    double distance = 8.0F;
                    Vector3d lookVec = entity.getLookAngle();
                    Vector3d pos = new Vector3d(lookVec.x * distance + offsetX, (double)entity.getEyeHeight() / (double)2.0F + lookVec.y * distance + offsetY, lookVec.z * distance + offsetZ);
                    if (target instanceof LivingEntity && isPositionInRoom(entity, abl, target.blockPosition()) || isPositionGriefable(entity).test(target.blockPosition())) {
                        AbilityHelper.setDeltaMovement(target, entity.position().add(pos).subtract(target.position()));
                        if (target instanceof ServerPlayerEntity) {
                            Set<SPlayerPositionLookPacket.Flags> flags = EnumSet.of(Flags.X, Flags.Y, Flags.Z);
                            ((ServerPlayerEntity)target).connection.teleport(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot, flags);
                        }
                    }

                    target.fallDistance = 0.0F;
                });
            } else {
                this.continuousComponent.stopContinuity(entity);
            }
        }
    }

    private static boolean isPositionInRoom(LivingEntity entity, RoomAbility abl, BlockPos pos) {
        boolean positionInRoom;
        if (abl != null) {
            positionInRoom = abl.isPositionInRoom(pos);
        } else {
            positionInRoom = DevilFruitCapability.get(entity).hasAwakenedFruit();
        }
        return positionInRoom;
    }

    private void onContinuityEnd(LivingEntity entity, IAbility ability) {
        this.grabbedEntities.stream().filter(Entity::isNoGravity).forEach((e) -> e.setNoGravity(false));
        this.grabbedEntities.clear();
        super.cooldownComponent.startCooldown(entity, COOLDOWN);
    }

    private static Predicate<BlockPos> isPositionGriefable(LivingEntity entity) {
        RoomAbility abl = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        ProtectedAreasData worldData = ProtectedAreasData.get(entity.level);
        return (pos) -> {
            boolean isGriefDisabled = !CommonConfig.INSTANCE.isAbilityGriefingEnabled();
            if (isGriefDisabled) {
                return false;
            } else {
                ProtectedArea area = worldData.getProtectedArea(pos.getX(), pos.getY(), pos.getZ());
                if (area != null && !area.canDestroyBlocks()) {
                    return false;
                } else if (!isPositionInRoom(entity, abl, pos)) {
                    return false;
                } else {
                    BlockState state = entity.level.getBlockState(pos);
                    boolean isBlockBanned = RestrictedBlockProtectionRule.INSTANCE.isBanned(state);
                    return !isBlockBanned;
                }
            }
        };
    }

    static {
        INSTANCE = new AbilityCore.Builder<>("Takt", AbilityCategory.DEVIL_FRUITS, ReworkedTaktAbility::new).addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(new AbilityDescriptionLine.IDescriptionLine[]{AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(240.0F), ContinuousComponent.getTooltip(60.0F)}).build();
    }
}