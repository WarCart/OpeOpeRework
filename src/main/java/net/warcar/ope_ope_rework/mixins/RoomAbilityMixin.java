package net.warcar.ope_ope_rework.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.ope_ope_rework.abilities.IRoomMixin;
import net.warcar.ope_ope_rework.config.CommonConfig;
import net.warcar.ope_ope_rework.init.Abilities;
import net.warcar.ope_ope_rework.projectiles.RoomProjectile;
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
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.PoolComponent;
import xyz.pixelatedw.mineminenomi.api.util.Interval;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

@Mixin(RoomAbility.class)
public abstract class RoomAbilityMixin extends Ability implements IRoomMixin {
    @Shadow @Final private ChargeComponent chargeComponent;
    @Shadow @Final private ContinuousComponent continuousComponent;
    @Shadow private int roomSize;
    @Shadow private Interval checkPositionInterval;
    private final AltModeComponent<RoomProjectile.RoomMode> modeComponent = new AltModeComponent<>(this, RoomProjectile.RoomMode.class, RoomProjectile.RoomMode.STATIC_ROOM);
    private final PoolComponent poolComponent = new PoolComponent(this, Abilities.ROOMS);

    @Shadow public abstract boolean isPositionInRoom(BlockPos pos);

    @Shadow public abstract int getROOMSize();

    private RoomProjectile room;

    public RoomAbilityMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void addComps(AbilityCore<RoomAbility> core, CallbackInfo ci) {
        this.addComponents(modeComponent, poolComponent);
        modeComponent.addChangeModeEvent((entity, iAbility, roomMode) -> {
            if (!DevilFruitCapability.get(entity).hasAwakenedFruit() && roomMode == RoomProjectile.RoomMode.R_ROOM) {
                throw new IllegalStateException("Mode Requirement isn't met");
            }
        });
    }

    @Inject(method = "onUseEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemPreStart(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        if (this.modeComponent.isMode(RoomProjectile.RoomMode.R_ROOM)) {
            ci.cancel();
            EntityRayTraceResult rayTraceEntities = WyHelper.rayTraceEntities(entity, 20);
            if (this.continuousComponent.isContinuous()) {
                this.continuousComponent.stopContinuity(entity);
            } else if (rayTraceEntities.getEntity() == null) {
                entity.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NO_TARGET), Util.NIL_UUID);
            } else {
                room = new RoomProjectile(entity.level, entity);
                room.setMaxSize(14);
                this.roomSize = 14;
                room.setTarget(rayTraceEntities.getEntity());
                room.tick();
                entity.level.addFreshEntity(room);
                this.continuousComponent.startContinuity(entity, -1);
                this.poolComponent.startPoolInUse(entity);
            }
        }
    }

    @Inject(method = "onChargeEnd", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemStart(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();
        if (!entity.level.isClientSide) {
            if (room != null) {
                room.remove();
            }
            if (this.modeComponent.isMode(RoomProjectile.RoomMode.STATIC_ROOM)) {
                room = new RoomProjectile(entity.level, entity);
                this.roomSize = (int) Math.max(8, (45.0F * this.chargeComponent.getChargeTime() / this.chargeComponent.getMaxChargeTime()));
                room.setMaxSize(this.roomSize);
                room.setSize(1);
                room.setPos(entity.getX(), entity.getY() - roomSize, entity.getZ());
                entity.level.addFreshEntity(room);
                entity.level.playSound(null, entity.blockPosition(), ModSounds.ROOM_EXPAND_SFX.get(), SoundCategory.PLAYERS, 5.0F, 1.0F);
                this.continuousComponent.startContinuity(entity, -1.0F);
            }
        }
    }

    @Inject(method = "onContinuityTick", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemTick(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        ci.cancel();
        if (!entity.level.isClientSide) {
            if (this.room != null && this.checkPositionInterval.canTick() && !this.isPositionInRoom(entity.blockPosition()) && this.modeComponent.isMode(RoomProjectile.RoomMode.STATIC_ROOM)) {
                this.continuousComponent.stopContinuity(entity);
            }
            this.poolComponent.startPoolInUse(entity);
        }
    }

    @Inject(method = "onContinuityEnd", at = @At("HEAD"), remap = false)
    private void newSystemEnd(LivingEntity entity, IAbility ability, CallbackInfo ci) {
        if (!entity.level.isClientSide) {
            this.room.remove();
            this.room = null;
            this.poolComponent.stopPoolInUse(entity);
        }
    }

    @Inject(method = "getROOMSize", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemSize(CallbackInfoReturnable<Integer> cir) {
        if (!this.isContinuous()) {
            cir.setReturnValue(40);
        }
    }

    @Inject(method = "isPositionInRoom", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemPos(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((this.room != null && pos.closerThan(this.room.position().add(0, this.getROOMSize(), 0), this.roomSize)) || (!this.isContinuous() && CommonConfig.INSTANCE.isOutsideAbilities()
                && DevilFruitCapability.get(Minecraft.getInstance().player).hasAwakenedFruit()));
    }

    @Inject(method = "getCenterBlock", at = @At("HEAD"), remap = false, cancellable = true)
    private void newCenter(CallbackInfoReturnable<BlockPos> cir) {
        if (this.room == null) {
            cir.setReturnValue(null);
        } else {
            cir.setReturnValue(this.room.blockPosition().above((int) (this.room.getSize() / 2)));
        }
    }

    public boolean isRRoom() {
        return this.modeComponent.isMode(RoomProjectile.RoomMode.R_ROOM);
    }
}
