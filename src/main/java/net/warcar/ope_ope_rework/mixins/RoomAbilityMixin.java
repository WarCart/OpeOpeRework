package net.warcar.ope_ope_rework.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
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
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.init.ModI18n;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

@Mixin(RoomAbility.class)
public abstract class RoomAbilityMixin extends ContinuousAbility implements IRoomMixin {
    @Shadow private int roomSize;

    @Shadow public abstract boolean isPositionInRoom(BlockPos pos);

    @Shadow public abstract int getROOMSize();

    private RoomProjectile room;

    private RoomProjectile.RoomMode mode = RoomProjectile.RoomMode.STATIC_ROOM;

    public RoomAbilityMixin(AbilityCore<? extends IAbility> core) {
        super(core);
    }

    @Inject(method = "onStartContinuityEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemPreStart(PlayerEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.mode.equals(RoomProjectile.RoomMode.R_ROOM)) {
            cir.setReturnValue(false);
            EntityRayTraceResult rayTraceEntities = WyHelper.rayTraceEntities(entity, 20);
            if (this.isContinuous()) {
                this.tryStoppingContinuity(entity);
            } else if (rayTraceEntities.getEntity() == null) {
                entity.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NO_TARGET), Util.NIL_UUID);
            } else {
                room = new RoomProjectile(entity.level, entity);
                room.setMaxSize(14);
                this.roomSize = 14;
                room.setTarget(rayTraceEntities.getEntity());
                room.tick();
                entity.level.addFreshEntity(room);
                this.setThreshold(-1);
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "beforeContinuityStopEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemStart(PlayerEntity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        if (!entity.level.isClientSide) {
            if (room != null) {
                room.remove();
            }
            if (this.mode.equals(RoomProjectile.RoomMode.STATIC_ROOM)) {
                room = new RoomProjectile(entity.level, entity);
                this.roomSize = (int) Math.max(8, (45.0F * this.continueTime / 20));
                room.setMaxSize(this.roomSize);
                room.setSize(1);
                room.setPos(entity.getX(), entity.getY() - roomSize, entity.getZ());
                entity.level.addFreshEntity(room);
                entity.level.playSound(null, entity.blockPosition(), ModSounds.ROOM_EXPAND_SFX.get(), SoundCategory.PLAYERS, 5.0F, 1.0F);
                this.setThreshold(0.0);
                WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(entity, this), entity);
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "duringContinuityEvent", at = @At("HEAD"), remap = false, cancellable = true)
    private void newSystemTick(PlayerEntity player, int continuousTime, CallbackInfo ci) {
        ci.cancel();
        if (!player.level.isClientSide) {
            if (this.room != null && continuousTime % 20 == 0 && !this.isPositionInRoom(player.blockPosition()) && this.mode.equals(RoomProjectile.RoomMode.STATIC_ROOM)) {
                this.tryStoppingContinuity(player);
            }
        }
    }

    @Inject(method = "afterContinuityStopEvent", at = @At("HEAD"), remap = false)
    private void newSystemEnd(PlayerEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!entity.level.isClientSide) {
            this.room.remove();
            this.room = null;
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
                && AwakeningDataCapability.get(Minecraft.getInstance().player).isAwakened()));
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
        return this.mode.equals(RoomProjectile.RoomMode.R_ROOM);
    }
}
